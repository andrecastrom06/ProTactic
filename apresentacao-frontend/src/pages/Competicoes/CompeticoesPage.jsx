import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { definirCapitao } from '../../services/capitaoService';
import { buscarTodasCompeticoes } from '../../services/competicaoService';
import { buscarInscricoesPorCompeticao } from '../../services/inscricaoService';
import { RegistrarInscricaoModal } from '../../components/RegistrarInscricaoModal/RegistrarInscricaoModal';
import { FaStar } from 'react-icons/fa';
import './CompeticoesPage.css'; 

// Componente para o Checkbox customizado
const CustomCheckbox = ({ checked }) => (
    <div className={`custom-checkbox ${checked ? 'checked' : ''}`}>
        {/* O '✔' é adicionado via CSS */}
    </div>
);

export const CompeticoesPage = () => {
    const { clubeIdLogado } = useAuth(); 
    const [activeTab, setActiveTab] = useState('atletas');
    const [atletas, setAtletas] = useState([]);
    const [competicoes, setCompeticoes] = useState([]);
    
    // --- Estados dinâmicos ---
    const [competicaoSelecionada, setCompeticaoSelecionada] = useState(null);
    const [isModalInscricaoOpen, setIsModalInscricaoOpen] = useState(false);
    // Guarda os NOMES dos atletas inscritos (para checagem rápida)
    const [inscricoes, setInscricoes] = useState(new Set()); 

    const [loadingAtletas, setLoadingAtletas] = useState(true);
    const [loadingCompeticoes, setLoadingCompeticoes] = useState(true);
    const [error, setError] = useState(null);

    // Carrega a lista de competições (para o dropdown)
    const carregarCompeticoes = useCallback(async () => {
        try {
            setLoadingCompeticoes(true);
            const dados = await buscarTodasCompeticoes();
            setCompeticoes(dados);
            if (dados.length > 0) {
                setCompeticaoSelecionada(dados[0]); // Seleciona a primeira por padrão
            }
        } catch (err) {
            setError("Não foi possível carregar as competições.");
        } finally {
            setLoadingCompeticoes(false);
        }
    }, []);

    // Carrega a lista de atletas inscritos na competição selecionada
    const carregarInscricoes = useCallback(async () => {
        if (!competicaoSelecionada) return; // Não faz nada se nenhuma competição estiver selecionada

        try {
            const dados = await buscarInscricoesPorCompeticao(competicaoSelecionada.nome);
            // Armazena apenas os nomes e que foram inscritos com sucesso
            const nomesInscritos = new Set(
                dados.filter(i => i.inscrito).map(i => i.atleta)
            );
            setInscricoes(nomesInscritos);
        } catch (err) {
            setError("Não foi possível carregar os atletas inscritos.");
        }
    }, [competicaoSelecionada]); // Depende da competição selecionada

    // Carrega os atletas do clube, e define seu status de "registrado"
    const carregarAtletas = useCallback(async () => {
        if (!clubeIdLogado || loadingCompeticoes) return; // Espera ter o clube e as competições

        try {
            setLoadingAtletas(true);
            const dadosApi = await buscarTodosJogadores();
            
            const atletasDoClube = dadosApi.filter(a => a.clubeId === clubeIdLogado);

            const atletasCompletos = atletasDoClube.map(atleta => ({
                ...atleta,
                // Lógica dinâmica: verifica se o nome do atleta está no Set de inscritos
                registrado: inscricoes.has(atleta.nome), 
            }));

            setAtletas(atletasCompletos);
            setError(null);
        } catch (err) {
            console.error("Erro ao buscar atletas:", err);
            setError("Não foi possível carregar os atletas.");
        } finally {
            setLoadingAtletas(false);
        }
    }, [clubeIdLogado, inscricoes, loadingCompeticoes]); // Depende do clube, das inscrições e do loading

    // --- Cadeia de Carregamento ---
    useEffect(() => {
        carregarCompeticoes();
    }, [carregarCompeticoes]);

    useEffect(() => {
        carregarInscricoes();
    }, [carregarInscricoes]); // Roda quando 'competicaoSelecionada' muda

    useEffect(() => {
        carregarAtletas();
    }, [carregarAtletas]); // Roda quando 'inscricoes' ou 'clubeIdLogado' mudam

    // Handler para definir o capitão (igual)
    const handleDefinirCapitao = async (jogadorId) => {
        if (!window.confirm("Tem certeza que deseja definir este jogador como capitão? O capitão anterior será substituído.")) {
            return;
        }
        try {
            await definirCapitao(jogadorId);
            alert("Capitão definido com sucesso!");
            carregarAtletas(); // Recarrega a lista
        } catch (err) {
            alert(`Erro ao definir capitão: ${err.message}`);
        }
    };
    
    // Handler para o seletor de competição (igual)
    const handleMudarCompeticao = (e) => {
        const competicaoId = parseInt(e.target.value, 10);
        const competicao = competicoes.find(c => c.id === competicaoId);
        setCompeticaoSelecionada(competicao);
    };
    
    // Handler para o sucesso da inscrição (atualizado)
    const handleInscricaoSuccess = (atletaNome) => {
        setIsModalInscricaoOpen(false);
        
        // Atualiza o estado localmente (mais rápido que refetch)
        setInscricoes(prev => new Set(prev).add(atletaNome));
        setAtletas(prevAtletas => 
            prevAtletas.map(a => 
                a.nome === atletaNome ? { ...a, registrado: true } : a
            )
        );
    };

    // Componente interno para a lista de atletas (aba principal)
    const AbaRegistroAtletas = () => (
        <div className="atleta-list-container">
            {/* Cabeçalho da Lista (Atualizado) */}
            <div className="atleta-list-item header">
                <span className="col-check">Registrado</span>
                <span className="col-nome">Nome</span>
                <span className="col-posicao">Posição</span>
                <span className="col-capitao">Capitão</span>
            </div>

            {/* Dados da Lista (Atualizado) */}
            {loadingAtletas ? (
                <p style={{ padding: '20px' }}>Carregando atletas...</p>
            ) : error ? (
                <p style={{ padding: '20px', color: 'red' }}>{error}</p>
            ) : (
                atletas.map((atleta) => (
                    // Adiciona a classe 'nao-registrado' se não estiver
                    <div key={atleta.id} className={`atleta-list-item ${!atleta.registrado ? 'nao-registrado' : ''}`}>
                        <span className="col-check">
                            {/* Usa o novo componente de checkbox */}
                            <CustomCheckbox checked={atleta.registrado} />
                        </span>
                        
                        <span className="col-nome">
                            {atleta.nome}
                            {atleta.capitao && atleta.registrado && <FaStar color="#f0ad4e" style={{ marginLeft: '8px' }} />}
                        </span>
                        <span className="col-posicao">{atleta.posicao}</span>
                        <span className="col-capitao">
                            {atleta.capitao && atleta.registrado ? (
                                <span className="capitao-badge">Capitão</span>
                            ) : (
                                <button 
                                    className="definir-btn"
                                    disabled={!atleta.registrado} // Regra: Desabilitado se não estiver registrado
                                    onClick={() => handleDefinirCapitao(atleta.id)}
                                >
                                    Definir
                                </button>
                            )}
                        </span>
                    </div>
                ))
            )}
        </div>
    );

    return (
        <div className="competicoes-page">
            <div className="competicao-header">
                <h2>Competição Ativa</h2>
                
                {/* Seletor de Competição (dinâmico) */}
                <div className="form-group">
                    <label>Selecionar Competição</label>
                    <select onChange={handleMudarCompeticao} value={competicaoSelecionada?.id || ''} disabled={loadingCompeticoes}>
                        {loadingCompeticoes && <option>Carregando...</option>}
                        {competicoes.map(comp => (
                            <option key={comp.id} value={comp.id}>
                                {comp.nome}
                            </option>
                        ))}
                    </select>
                </div>
                <span className="atletas-registrados">
                    Atletas Registrados: {inscricoes.size}/{atletas.length}
                </span>
            </div>

            {/* Container das Abas (igual) */}
            <div className="tabs-container">
                <div className="tab-buttons">
                    <button 
                        className={`tab-item ${activeTab === 'atletas' ? 'active' : ''}`}
                        onClick={() => setActiveTab('atletas')}
                    >
                        Registro de Atletas
                    </button>
                    <button 
                        className={`tab-item ${activeTab === 'cartoes' ? 'active' : ''}`}
                        onClick={() => setActiveTab('cartoes')}
                    >
                        Cartões e Suspensões
                    </button>
                    <button 
                        className={`tab-item ${activeTab === 'premiacoes' ? 'active' : ''}`}
                        onClick={() => setActiveTab('premiacoes')}
                    >
                        Premiações
                    </button>
                </div>
                
                {activeTab === 'atletas' && (
                     <button 
                        className="registrar-inscricao-btn"
                        onClick={() => setIsModalInscricaoOpen(true)}
                        disabled={!competicaoSelecionada} // Desabilita se competições não carregaram
                     >
                        + Registrar Inscrição
                    </button>
                )}
            </div>

            {/* Conteúdo das Abas (igual) */}
            <div className="tab-content">
                {activeTab === 'atletas' && <AbaRegistroAtletas />}
                
                {activeTab === 'cartoes' && (
                    <div className="placeholder-content">
                        (Aqui entrará a funcionalidade de Cartões e Suspensões)
                    </div>
                )}
                
                {activeTab === 'premiacoes' && (
                    <div className="placeholder-content">
                        (Aqui entrará a funcionalidade de Premiações)
                    </div>
                )}
            </div>

            {/* Novo Modal de Inscrição */}
            {isModalInscricaoOpen && competicaoSelecionada && (
                <RegistrarInscricaoModal
                    competicaoNome={competicaoSelecionada.nome}
                    onClose={() => setIsModalInscricaoOpen(false)}
                    onSuccess={handleInscricaoSuccess}
                    atletasJaInscritos={inscricoes} // Passa o Set de nomes
                />
            )}
        </div>
    );
};