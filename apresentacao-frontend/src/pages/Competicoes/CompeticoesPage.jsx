import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { definirCapitao } from '../../services/capitaoService';
import { buscarTodasCompeticoes } from '../../services/competicaoService';
import { buscarInscricoesPorCompeticao } from '../../services/inscricaoService';
import { buscarTodasPremiacoes } from '../../services/premiacaoService'; // NOVO

import { RegistrarInscricaoModal } from '../../components/RegistrarInscricaoModal/RegistrarInscricaoModal';
import { RegistrarPremiacaoModal } from '../../components/RegistrarPremiacaoModal'; // NOVO (caminho relativo se estiver na root de components)

import { FaStar, FaTrophy } from 'react-icons/fa';
import './CompeticoesPage.css'; 

const CustomCheckbox = ({ checked }) => (
    <div className={`custom-checkbox ${checked ? 'checked' : ''}`}></div>
);

export const CompeticoesPage = () => {
    const { clubeIdLogado } = useAuth(); 
    const [activeTab, setActiveTab] = useState('atletas');
    
    // Dados
    const [atletas, setAtletas] = useState([]);
    const [competicoes, setCompeticoes] = useState([]);
    const [premiacoes, setPremiacoes] = useState([]); // NOVO
    
    // Estados
    const [competicaoSelecionada, setCompeticaoSelecionada] = useState(null);
    const [isModalInscricaoOpen, setIsModalInscricaoOpen] = useState(false);
    const [isModalPremiacaoOpen, setIsModalPremiacaoOpen] = useState(false); // NOVO
    const [inscricoes, setInscricoes] = useState(new Set()); 

    const [loading, setLoading] = useState(true); // Simplifiquei para um loading geral
    const [error, setError] = useState(null);

    // --- 1. Carregar Dados Iniciais ---
    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            try {
                // Carrega competições
                const comps = await buscarTodasCompeticoes();
                setCompeticoes(comps);
                if(comps.length > 0) setCompeticaoSelecionada(comps[0]);

                // Carrega premiações (NOVO)
                const prems = await buscarTodasPremiacoes();
                setPremiacoes(prems);

            } catch(e) {
                console.error(e);
                setError("Erro ao carregar dados iniciais.");
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    // --- 2. Carregar Inscrições e Atletas (Quando muda competição ou clube) ---
    const carregarAtletasEInscricoes = useCallback(async () => {
        if (!competicaoSelecionada || !clubeIdLogado) return;

        try {
            // Busca inscrições da competição atual
            const inscricoesData = await buscarInscricoesPorCompeticao(competicaoSelecionada.nome);
            const nomesInscritos = new Set(inscricoesData.filter(i => i.inscrito).map(i => i.atleta));
            setInscricoes(nomesInscritos);

            // Busca todos atletas e filtra pelo meu clube
            const todosAtletas = await buscarTodosJogadores();
            const meusAtletas = todosAtletas.filter(a => a.clubeId === clubeIdLogado).map(atleta => ({
                ...atleta,
                registrado: nomesInscritos.has(atleta.nome)
            }));
            setAtletas(meusAtletas);

        } catch (e) {
            console.error(e);
        }
    }, [competicaoSelecionada, clubeIdLogado]);

    useEffect(() => {
        carregarAtletasEInscricoes();
    }, [carregarAtletasEInscricoes]);

    
    // --- Handlers ---
    
    const handleRecarregarPremiacoes = async () => {
        const prems = await buscarTodasPremiacoes();
        setPremiacoes(prems);
        setIsModalPremiacaoOpen(false);
    };

    const handleDefinirCapitao = async (jogadorId) => {
        if(!window.confirm("Definir novo capitão?")) return;
        try {
            await definirCapitao(jogadorId);
            alert("Sucesso!");
            carregarAtletasEInscricoes();
        } catch(e) { alert(e.message); }
    };


    // --- Componentes das Abas ---

    const AbaAtletas = () => (
        <div className="atleta-list-container">
            <div className="atleta-list-item header">
                <span className="col-check">Inscrito</span>
                <span className="col-nome">Nome</span>
                <span className="col-posicao">Posição</span>
                <span className="col-capitao">Capitão</span>
            </div>
            {atletas.map(atleta => (
                <div key={atleta.id} className={`atleta-list-item ${!atleta.registrado ? 'nao-registrado' : ''}`}>
                    <span className="col-check"><CustomCheckbox checked={atleta.registrado} /></span>
                    <span className="col-nome">
                        {atleta.nome}
                        {atleta.capitao && atleta.registrado && <FaStar color="#f0ad4e" style={{marginLeft:8}}/>}
                    </span>
                    <span className="col-posicao">{atleta.posicao}</span>
                    <span className="col-capitao">
                        {atleta.capitao && atleta.registrado ? <span className="capitao-badge">Capitão</span> : 
                         <button className="definir-btn" disabled={!atleta.registrado} onClick={() => handleDefinirCapitao(atleta.id)}>Definir</button>
                        }
                    </span>
                </div>
            ))}
        </div>
    );

    // --- ABA DE PREMIAÇÕES (NOVA) ---
    const AbaPremiacoes = () => {
        // Filtra premiações para mostrar apenas as que pertencem aos jogadores do meu clube
        // (Opcional: se quiseres ver todas, remove o filtro)
        const minhasPremiacoes = premiacoes.filter(p => 
            atletas.some(a => a.id === p.jogadorId)
        );

        return (
            <div className="atleta-list-container">
                <div className="atleta-list-item header" style={{gridTemplateColumns: '2fr 2fr 1fr'}}>
                    <span>Prémio</span>
                    <span>Jogador Vencedor</span>
                    <span>Data</span>
                </div>
                {minhasPremiacoes.length === 0 ? (
                    <div style={{padding:20, textAlign:'center', color:'#777'}}>Nenhuma premiação registrada.</div>
                ) : (
                    minhasPremiacoes.map(p => {
                        // Encontra o nome do jogador na lista de atletas carregada
                        const jogador = atletas.find(a => a.id === p.jogadorId);
                        const nomeJogador = jogador ? jogador.nome : `ID: ${p.jogadorId}`;
                        return (
                            <div key={p.id} className="atleta-list-item" style={{gridTemplateColumns: '2fr 2fr 1fr'}}>
                                <span style={{fontWeight:'bold', color:'#004d40'}}><FaTrophy style={{marginRight:8}}/> {p.nome}</span>
                                <span>{nomeJogador}</span>
                                <span>{new Date(p.dataPremiacao).toLocaleDateString()}</span>
                            </div>
                        );
                    })
                )}
            </div>
        );
    };

    if (loading) return <div style={{padding:20}}>Carregando...</div>;

    return (
        <div className="competicoes-page">
            <div className="competicao-header">
                <h2>Competição Ativa</h2>
                <div className="form-group">
                    <label>Selecionar Competição</label>
                    <select 
                        onChange={e => setCompeticaoSelecionada(competicoes.find(c => c.id === parseInt(e.target.value)))} 
                        value={competicaoSelecionada?.id || ''}
                    >
                        {competicoes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                    </select>
                </div>
                <span className="atletas-registrados">Inscritos: {inscricoes.size}/{atletas.length}</span>
            </div>

            <div className="tabs-container">
                <div className="tab-buttons">
                    <button className={`tab-item ${activeTab === 'atletas' ? 'active' : ''}`} onClick={() => setActiveTab('atletas')}>Registro de Atletas</button>
                    <button className={`tab-item ${activeTab === 'premiacoes' ? 'active' : ''}`} onClick={() => setActiveTab('premiacoes')}>Premiações</button>
                </div>
                
                {/* Botões de Ação Variáveis */}
                {activeTab === 'atletas' && (
                     <button className="registrar-inscricao-btn" onClick={() => setIsModalInscricaoOpen(true)} disabled={!competicaoSelecionada}>
                        + Registrar Inscrição
                    </button>
                )}
                {activeTab === 'premiacoes' && (
                     <button className="registrar-inscricao-btn" onClick={() => setIsModalPremiacaoOpen(true)}>
                        + Nova Premiação
                    </button>
                )}
            </div>

            <div className="tab-content">
                {activeTab === 'atletas' && <AbaAtletas />}
                {activeTab === 'premiacoes' && <AbaPremiacoes />}
            </div>

            {isModalInscricaoOpen && (
                <RegistrarInscricaoModal
                    competicaoNome={competicaoSelecionada?.nome}
                    onClose={() => setIsModalInscricaoOpen(false)}
                    onSuccess={() => { setIsModalInscricaoOpen(false); carregarAtletasEInscricoes(); }}
                    atletasJaInscritos={inscricoes}
                />
            )}

            {/* Modal de Premiação */}
            {isModalPremiacaoOpen && (
                <RegistrarPremiacaoModal
                    onClose={() => setIsModalPremiacaoOpen(false)}
                    onSuccess={handleRecarregarPremiacoes}
                />
            )}
        </div>
    );
};