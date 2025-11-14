import React, { useState, useEffect, useCallback } from 'react';
import { 
    buscarPropostasRecebidas,
    aceitarProposta,      
    recusarProposta       
} from '../../services/propostaService';
import { buscarContratosVigentes } from '../../services/contratoService';
import { NovaPropostaModal } from '../../components/NovaPropostaModal';
import { useAuth } from '../../store/AuthContext';
import './ContratosPage.css'; 

// Função auxiliar para os "badges" de status
const getStatusClass = (status) => {
    switch (status) {
        case 'PENDENTE': return 'propostas-status-pendente';
        case 'ACEITE': return 'propostas-status-aceite';
        case 'RECUSADA': return 'propostas-status-rejeitado';
        case 'ATIVO': return 'propostas-status-aceite';
        case 'VENCENDO': return 'propostas-status-pendente';
        default: return 'propostas-status-default';
    }
};

// Função auxiliar para calcular o status e a data fim
const calcularInfoContrato = (dataInicioStr, duracaoMeses) => {
    if (!dataInicioStr || !duracaoMeses) {
        return { dataFim: 'N/A', status: 'Indefinido', diasRestantes: 0 };
    }
    
    try {
        const dataInicio = new Date(dataInicioStr);
        // Corrige o bug do fuso horário (converte a data 'YYYY-MM-DD' para o fuso local)
        const dataInicioUTC = new Date(dataInicio.getUTCFullYear(), dataInicio.getUTCMonth(), dataInicio.getUTCDate());

        const dataFim = new Date(dataInicioUTC);
        dataFim.setMonth(dataFim.getMonth() + duracaoMeses);
        
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0); // Zera a hora de hoje para a comparação
        
        const diffTime = dataFim - hoje;
        const diasRestantes = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        let status = "ATIVO";
        if (diasRestantes <= 0) {
            status = "EXPIRADO";
        } else if (diasRestantes <= 365) {
            status = "VENCENDO";
        }
        
        return {
            dataFim: dataFim.toLocaleDateString('pt-BR'),
            status: status,
            diasRestantes: diasRestantes > 0 ? diasRestantes : 0
        };
    } catch (e) {
        return { dataFim: 'Inválida', status: 'Erro', diasRestantes: 0 };
    }
};

// Estilos dos botões (igual)
const styles = {
    buttonAprovar: { backgroundColor: '#E0F8E0', color: '#006400', border: '1px solid #006400', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer', marginRight: '5px' },
    buttonRejeitar: { backgroundColor: '#FDE0E0', color: '#A00000', border: '1px solid #A00000', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer' },
    buttonNovaProposta: { backgroundColor: '#00796b', color: 'white', border: 'none', padding: '10px 15px', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }
};

export const ContratosPage = () => {
    
    const { clubeIdLogado } = useAuth();
    
    const [activeTab, setActiveTab] = useState('vigentes');
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    const [propostas, setPropostas] = useState([]);
    const [contratos, setContratos] = useState([]);
    
    const [carregandoPropostas, setCarregandoPropostas] = useState(true);
    const [carregandoContratos, setCarregandoContratos] = useState(true);
    const [erro, setErro] = useState(null);

    const carregarPropostas = useCallback(async () => {
        if (!clubeIdLogado) return;
        try {
            setCarregandoPropostas(true); setErro(null);
            const dados = await buscarPropostasRecebidas(clubeIdLogado);
            setPropostas(dados);
        } catch (err) { setErro(err.message); } 
        finally { setCarregandoPropostas(false); }
    }, [clubeIdLogado]); 

    const carregarContratos = useCallback(async () => {
        if (!clubeIdLogado) return;
        try {
            setCarregandoContratos(true); setErro(null);
            const dados = await buscarContratosVigentes(clubeIdLogado);
            setContratos(dados);
        } catch (err) { setErro(err.message); } 
        finally { setCarregandoContratos(false); }
    }, [clubeIdLogado]); 

    useEffect(() => {
        carregarPropostas();
        carregarContratos();
    }, [carregarPropostas, carregarContratos]); 
    
    // --- (CORREÇÃO - BUG 1) ---
    // Funções de clique completas (não mais resumidas)
    const handleAceitar = async (propostaId) => {
        if (!window.confirm("Tem a certeza que deseja ACEITAR esta proposta? Esta ação irá transferir o jogador.")) {
            return;
        }
        try {
            await aceitarProposta(propostaId);
            alert("Proposta aceite com sucesso! O jogador foi transferido.");
            carregarPropostas(); // Recarrega as propostas
            carregarContratos(); // Recarrega os contratos (o jogador agora é seu)
        } catch (err) {
            alert(`Erro ao aceitar proposta: ${err.message}`);
        }
    };

    const handleRejeitar = async (propostaId) => {
        if (!window.confirm("Tem a certeza que deseja REJEITAR esta proposta?")) {
            return;
        }
        try {
            await recusarProposta(propostaId);
            alert("Proposta rejeitada com sucesso!");
            carregarPropostas(); // Recarrega as propostas
        } catch (err) {
            alert(`Erro ao rejeitar proposta: ${err.message}`);
        }
    };
    
    const handleProposalSuccess = () => {
        setIsModalOpen(false);
        carregarPropostas();
    };
    // --- (FIM DA CORREÇÃO - BUG 1) ---

    
    // --- Lógica dos Cards (agora dinâmica) ---
    const contratosVencendo = contratos.filter(c => {
        const { diasRestantes } = calcularInfoContrato(c.dataInicio, c.duracaoMeses);
        return diasRestantes > 0 && diasRestantes <= 365;
    }).length;

    const propostasPendentes = propostas.filter(p => p.status === 'PENDENTE').length;

    
    // --- Componente Interno: Tabela de Propostas ---
    const TabelaPropostas = () => (
        <table className="propostas-table">
            <thead>
                <tr>
                    <th>Atleta</th> <th>Posição</th> <th>Idade</th>
                    <th>Clube Atual</th> <th>Salário Proposto</th> <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                {carregandoPropostas ? (
                    <tr><td colSpan="7" style={{ textAlign: 'center' }}>Carregando propostas...</td></tr>
                ) : propostas.length === 0 ? (
                    <tr><td colSpan="7" style={{ textAlign: 'center' }}>Nenhuma proposta recebida.</td></tr>
                ) : (
                    propostas.map((proposta) => (
                        <tr key={proposta.id}>
                            <td>{proposta.atletaNome}</td>
                            <td>{proposta.atletaPosicao}</td>
                            <td>{proposta.atletaIdade}</td>
                            <td>{proposta.clubeAtualNome || 'Livre'}</td>
                            <td>R$ {proposta.valor.toFixed(2)}</td>
                            <td><span className={`propostas-status ${getStatusClass(proposta.status)}`}>{proposta.status}</span></td>
                            <td>
                                {proposta.status === 'PENDENTE' && (
                                    <>
                                        {/* Os botões agora chamam os handlers corretos */}
                                        <button className="propostas-btn propostas-btn-aprovar" onClick={() => handleAceitar(proposta.id)}>Aprovar</button>
                                        <button className="propostas-btn propostas-btn-rejeitar" onClick={() => handleRejeitar(proposta.id)}>Rejeitar</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))
                )}
            </tbody>
        </table>
    );

    // --- Componente Interno: Tabela de Contratos (COM DATAS CORRIGIDAS) ---
    const TabelaContratosVigentes = () => (
        <table className="propostas-table">
            <thead>
                <tr>
                    <th>Atleta</th> <th>Posição</th> <th>Data Início</th>
                    <th>Data Fim</th>
                    <th>Salário</th> <th>Status</th> <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                {carregandoContratos ? (
                    <tr><td colSpan="7" style={{ textAlign: 'center' }}>Carregando contratos...</td></tr>
                ) : contratos.length === 0 ? (
                    <tr><td colSpan="7" style={{ textAlign: 'center' }}>Nenhum contrato vigente encontrado.</td></tr>
                ) : (
                    contratos.map((contrato) => {
                        // --- (CORREÇÃO - BUG 2) ---
                        // Calcula a data fim e o status dinamicamente
                        const { dataFim, status, diasRestantes } = calcularInfoContrato(contrato.dataInicio, contrato.duracaoMeses);
                        // --- (FIM DA CORREÇÃO - BUG 2) ---

                        return (
                            <tr key={contrato.id}>
                                <td>{contrato.atletaNome}</td>
                                <td>{contrato.atletaPosicao}</td>
                                <td>{contrato.dataInicio ? new Date(contrato.dataInicio).toLocaleDateString('pt-BR') : 'N/A'}</td>
                                <td>{dataFim} ({diasRestantes} dias)</td>
                                <td>R$ {contrato.salario.toFixed(2)}</td>
                                <td><span className={`propostas-status ${getStatusClass(status)}`}>{status}</span></td>
                                <td>
                                    {status !== 'EXPIRADO' && <button className="propostas-btn propostas-btn-aprovar">Renovar</button>}
                                    <button className="propostas-btn propostas-btn-rejeitar">Encerrar</button>
                                </td>
                            </tr>
                        );
                    })
                )}
            </tbody>
        </table>
    );

    // --- Renderização Principal da Página ---
    return (
        <div className="propostas-page">
            
            <h1>Gestão de Contratos</h1>
            <p className="page-subtitle">Gerencie contratos e propostas de atletas</p>

            {/* A caixa amarela foi removida */}

            {/* Stats Cards (agora dinâmicos) */}
            <div className="stats-container">
                <div className="stat-card">
                    <span className="stat-label">Contratos Ativos</span>
                    <span className="stat-value">{carregandoContratos ? '...' : contratos.length}</span>
                    <span className="stat-desc">Vigentes atualmente</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Vencendo em Breve</span>
                    <span className="stat-value">{carregandoContratos ? '...' : contratosVencendo}</span>
                    <span className="stat-desc">Próximos 12 meses</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Propostas Abertas</span>
                    <span className="stat-value">{carregandoPropostas ? '...' : propostasPendentes}</span>
                    <span className="stat-desc">Aguardando decisão</span>
                </div>
            </div>

            {/* Tab Slider (corrigido para o bug 4) */}
            <div className="tab-slider">
                <div className="tab-buttons">
                    <button 
                        className={`tab-btn ${activeTab === 'vigentes' ? 'active' : ''}`}
                        onClick={() => setActiveTab('vigentes')}
                    >
                        Contratos Vigentes
                    </button>
                    <button 
                        className={`tab-btn ${activeTab === 'propostas' ? 'active' : ''}`}
                        onClick={() => setActiveTab('propostas')}
                    >
                        Propostas de Contratação
                    </button>
                </div>

                {/* Botão de Nova Proposta (só aparece na tab 'propostas') */}
                {activeTab === 'propostas' && (
                    <button 
                        style={styles.buttonNovaProposta}
                        onClick={() => setIsModalOpen(true)}
                    >
                        + Nova Proposta
                    </button>
                )}
            </div>

            {/* Conteúdo Condicional */}
            <div className="tab-content">
                {activeTab === 'propostas' ? <TabelaPropostas /> : <TabelaContratosVigentes />}
            </div>

            {/* O Modal (passa o clubeIdLogado) */}
            {isModalOpen && (
                <NovaPropostaModal 
                    onClose={() => setIsModalOpen(false)}
                    onSuccess={handleProposalSuccess}
                    clubePropositorId={clubeIdLogado}
                />
            )}
        </div>
    );
};