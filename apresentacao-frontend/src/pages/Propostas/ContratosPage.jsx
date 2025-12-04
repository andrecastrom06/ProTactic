import React, { useState, useEffect, useCallback } from 'react';
import Swal from 'sweetalert2';
import { 
    buscarPropostasRecebidas,
    aceitarProposta,      
    recusarProposta       
} from '../../services/propostaService';
import { buscarContratosVigentes } from '../../services/contratoService';

import { NovaPropostaModal } from '../../components/NovaPropostaModal';
import { RenovarContratoModal } from '../../components/RenovarContratoModal'; 
import { ConfirmarDispensaModal } from '../../components/ConfirmarDispensaModal'; 

import { useAuth } from '../../store/AuthContext';
import './ContratosPage.css'; 

const getStatusClass = (status) => {
    switch (status) {
        case 'PENDENTE': return 'propostas-status-pendente';
        case 'ACEITE': return 'propostas-status-aceite';
        case 'RECUSADA': return 'propostas-status-rejeitado';
        case 'ATIVO': return 'propostas-status-aceite';
        case 'VENCENDO': return 'propostas-status-pendente';
        case 'RESCINDIDO': return 'propostas-status-rejeitado';
        default: return 'propostas-status-default';
    }
};

const calcularInfoContrato = (dataInicioStr, duracaoMeses) => {
    if (!dataInicioStr || !duracaoMeses) {
        return { dataFim: 'N/A', status: 'Indefinido', diasRestantes: 0 };
    }
    try {
        const dataInicio = new Date(dataInicioStr);
        const dataInicioUTC = new Date(dataInicio.getUTCFullYear(), dataInicio.getUTCMonth(), dataInicio.getUTCDate());
        const dataFim = new Date(dataInicioUTC);
        dataFim.setMonth(dataFim.getMonth() + duracaoMeses);
        
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0); 
        
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

const styles = {
    buttonNovaProposta: { backgroundColor: '#00796b', color: 'white', border: 'none', padding: '10px 15px', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }
};

export const ContratosPage = () => {
    const { clubeIdLogado } = useAuth();
    const [activeTab, setActiveTab] = useState('vigentes');
    
    // Estados dos Modais
    const [isNovaPropostaOpen, setIsNovaPropostaOpen] = useState(false);
    const [isRenovarOpen, setIsRenovarOpen] = useState(false);   
    const [isEncerrarOpen, setIsEncerrarOpen] = useState(false); 
    const [contratoSelecionado, setContratoSelecionado] = useState(null); 

    const [propostas, setPropostas] = useState([]);
    const [contratos, setContratos] = useState([]);
    
    // Removido estados de loading individuais para simplificar, mas mantidos na lógica se desejar usar spinners
    const [carregandoPropostas, setCarregandoPropostas] = useState(true);
    const [carregandoContratos, setCarregandoContratos] = useState(true);
    
    const carregarPropostas = useCallback(async () => {
        if (!clubeIdLogado) return;
        try {
            setCarregandoPropostas(true);
            const dados = await buscarPropostasRecebidas(clubeIdLogado);
            setPropostas(dados);
        } catch (err) { console.error(err); } 
        finally { setCarregandoPropostas(false); }
    }, [clubeIdLogado]); 

    const carregarContratos = useCallback(async () => {
        if (!clubeIdLogado) return;
        try {
            setCarregandoContratos(true);
            const dados = await buscarContratosVigentes(clubeIdLogado);
            setContratos(dados.filter(c => c.status !== 'RESCINDIDO'));
        } catch (err) { console.error(err); } 
        finally { setCarregandoContratos(false); }
    }, [clubeIdLogado]); 

    useEffect(() => {
        carregarPropostas();
        carregarContratos();
    }, [carregarPropostas, carregarContratos]); 

    const abrirModalRenovar = (contrato) => {
        setContratoSelecionado(contrato);
        setIsRenovarOpen(true);
    };

    const abrirModalEncerrar = (contrato) => {
        setContratoSelecionado(contrato);
        setIsEncerrarOpen(true);
    };

    const handleSuccessContrato = () => {
        carregarContratos();
    };

    const handleAceitar = async (propostaId) => {
        const result = await Swal.fire({
            title: 'Aceitar Proposta?',
            text: "O jogador será contratado e adicionado ao elenco.",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#00796b',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sim, aceitar',
            cancelButtonText: 'Cancelar'
        });

        if (!result.isConfirmed) return;

        try {
            await aceitarProposta(propostaId);
            
            await Swal.fire({
                title: 'Proposta Aceita!',
                text: 'Novo contrato gerado com sucesso.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            carregarPropostas(); 
            carregarContratos(); 
        } catch (err) { 
            // 3. Erro
            Swal.fire({
                title: 'Erro',
                text: err.message || "Não foi possível aceitar a proposta.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };

    const handleRejeitar = async (propostaId) => {
        const result = await Swal.fire({
            title: 'Rejeitar Proposta?',
            text: "Esta ação não pode ser desfeita.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sim, rejeitar',
            cancelButtonText: 'Cancelar'
        });

        if (!result.isConfirmed) return;

        try {
            await recusarProposta(propostaId);
            
            await Swal.fire({
                title: 'Rejeitada',
                text: 'A proposta foi recusada.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });
            
            carregarPropostas(); 
        } catch (err) { 
            Swal.fire({
                title: 'Erro',
                text: err.message || "Não foi possível rejeitar a proposta.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };

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
                {propostas.length === 0 ? <tr><td colSpan="7" style={{textAlign:'center'}}>Nenhuma proposta.</td></tr> :
                    propostas.map((p) => (
                        <tr key={p.id}>
                            <td>{p.atletaNome}</td> <td>{p.atletaPosicao}</td> <td>{p.atletaIdade}</td>
                            <td>{p.clubeAtualNome || 'Livre'}</td> <td>R$ {p.valor.toFixed(2)}</td>
                            <td><span className={`propostas-status ${getStatusClass(p.status)}`}>{p.status}</span></td>
                            <td>
                                {p.status === 'PENDENTE' && (
                                    <>
                                        <button className="propostas-btn propostas-btn-aprovar" onClick={() => handleAceitar(p.id)}>Aprovar</button>
                                        <button className="propostas-btn propostas-btn-rejeitar" onClick={() => handleRejeitar(p.id)}>Rejeitar</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))
                }
            </tbody>
        </table>
    );

    const TabelaContratosVigentes = () => (
        <table className="propostas-table">
            <thead>
                <tr>
                    <th>Atleta</th> <th>Posição</th> <th>Início</th>
                    <th>Fim (Dias)</th> <th>Salário</th> <th>Status</th> <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                {contratos.length === 0 ? <tr><td colSpan="7" style={{textAlign:'center'}}>Nenhum contrato ativo.</td></tr> :
                    contratos.map((c) => {
                        const { dataFim, status, diasRestantes } = calcularInfoContrato(c.dataInicio, c.duracaoMeses);
                        return (
                            <tr key={c.id}>
                                <td>{c.atletaNome}</td> <td>{c.atletaPosicao}</td>
                                <td>{c.dataInicio ? new Date(c.dataInicio).toLocaleDateString('pt-BR') : '-'}</td>
                                <td>{dataFim} ({diasRestantes}d)</td>
                                <td>R$ {c.salario.toFixed(2)}</td>
                                <td><span className={`propostas-status ${getStatusClass(status)}`}>{status}</span></td>
                                <td>
                                    <button className="propostas-btn propostas-btn-aprovar" onClick={() => abrirModalRenovar(c)}>Renovar</button>
                                    <button className="propostas-btn propostas-btn-rejeitar" onClick={() => abrirModalEncerrar(c)}>Encerrar</button>
                                </td>
                            </tr>
                        );
                    })
                }
            </tbody>
        </table>
    );

    return (
        <div className="propostas-page">
            <h1>Gestão de Contratos</h1>
            <div className="stats-container">
                <div className="stat-card"> <span className="stat-label">Contratos Ativos</span> <span className="stat-value">{contratos.length}</span> </div>
                <div className="stat-card"> <span className="stat-label">Propostas Pendentes</span> <span className="stat-value">{propostas.filter(p => p.status === 'PENDENTE').length}</span> </div>
            </div>

            <div className="tab-slider">
                <div className="tab-buttons">
                    <button className={`tab-btn ${activeTab === 'vigentes' ? 'active' : ''}`} onClick={() => setActiveTab('vigentes')}>Contratos Vigentes</button>
                    <button className={`tab-btn ${activeTab === 'propostas' ? 'active' : ''}`} onClick={() => setActiveTab('propostas')}>Propostas</button>
                </div>
                {activeTab === 'propostas' && (
                    <button style={styles.buttonNovaProposta} onClick={() => setIsNovaPropostaOpen(true)}>+ Nova Proposta</button>
                )}
            </div>

            <div className="tab-content">
                {activeTab === 'propostas' ? <TabelaPropostas /> : <TabelaContratosVigentes />}
            </div>

            {/* Modais */}
            {isNovaPropostaOpen && (
                <NovaPropostaModal onClose={() => setIsNovaPropostaOpen(false)} onSuccess={() => carregarPropostas()} clubePropositorId={clubeIdLogado} />
            )}
            
            {isRenovarOpen && contratoSelecionado && (
                <RenovarContratoModal 
                    contrato={contratoSelecionado} 
                    onClose={() => setIsRenovarOpen(false)} 
                    onSuccess={handleSuccessContrato} 
                />
            )}

            {isEncerrarOpen && contratoSelecionado && (
                <ConfirmarDispensaModal 
                    contrato={contratoSelecionado} 
                    onClose={() => setIsEncerrarOpen(false)} 
                    onSuccess={handleSuccessContrato} 
                />
            )}
        </div>
    );
};