import React, { useState, useEffect, useCallback } from 'react';
import Swal from 'sweetalert2';
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { definirCapitao } from '../../services/capitaoService';
import { buscarTodasCompeticoes } from '../../services/competicaoService';
import { buscarInscricoesPorCompeticao } from '../../services/inscricaoService';
import { buscarTodasPremiacoes } from '../../services/premiacaoService';

import { RegistrarInscricaoModal } from '../../components/RegistrarInscricaoModal/RegistrarInscricaoModal';
import { RegistrarPremiacaoModal } from '../../components/RegistrarPremiacaoModal'; 

import { FaStar, FaTrophy, FaMoneyBillWave } from 'react-icons/fa';
import './CompeticoesPage.css'; 

const CustomCheckbox = ({ checked }) => (
    <div className={`custom-checkbox ${checked ? 'checked' : ''}`}></div>
);

const formatarDataSemFuso = (dataString) => {
    if (!dataString) return '-';
    const date = new Date(dataString);
    return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(date);
};

export const CompeticoesPage = () => {
    const { clubeIdLogado } = useAuth(); 
    const [activeTab, setActiveTab] = useState('atletas');
    
    // Dados
    const [atletas, setAtletas] = useState([]);
    const [competicoes, setCompeticoes] = useState([]);
    const [premiacoes, setPremiacoes] = useState([]); 
    
    // Estados
    const [competicaoSelecionada, setCompeticaoSelecionada] = useState(null);
    const [isModalInscricaoOpen, setIsModalInscricaoOpen] = useState(false);
    const [isModalPremiacaoOpen, setIsModalPremiacaoOpen] = useState(false); 
    const [inscricoes, setInscricoes] = useState(new Set()); 

    const [loading, setLoading] = useState(true); 
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            try {
                const comps = await buscarTodasCompeticoes();
                setCompeticoes(comps);
                if(comps.length > 0) setCompeticaoSelecionada(comps[0]);

                const prems = await buscarTodasPremiacoes();
                setPremiacoes(prems);

            } catch(e) {
                console.error(e);
                setError("Erro ao carregar dados iniciais.");
                
                Swal.fire({
                    icon: 'error',
                    title: 'Erro de Conexão',
                    text: 'Não foi possível carregar as competições e premiações.'
                });
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    const carregarAtletasEInscricoes = useCallback(async () => {
        if (!competicaoSelecionada || !clubeIdLogado) return;

        try {
            const inscricoesData = await buscarInscricoesPorCompeticao(competicaoSelecionada.nome);
            const nomesInscritos = new Set(inscricoesData.filter(i => i.inscrito).map(i => i.atleta));
            setInscricoes(nomesInscritos);

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

    
    const handleRecarregarPremiacoes = async () => {
        const prems = await buscarTodasPremiacoes();
        setPremiacoes(prems);
        setIsModalPremiacaoOpen(false);
    };

    const handleDefinirCapitao = async (jogadorId) => {
        const result = await Swal.fire({
            title: 'Definir Capitão?',
            text: "Este jogador passará a ser o líder da equipe.",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#f0ad4e',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sim, definir',
            cancelButtonText: 'Cancelar'
        });

        if (!result.isConfirmed) return;

        try {
            await definirCapitao(jogadorId);
            await Swal.fire({
                title: 'Novo Capitão!',
                text: 'A braçadeira foi passada com sucesso.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            carregarAtletasEInscricoes();
        } catch(e) { 
            Swal.fire({
                title: 'Erro',
                text: e.message || "Não foi possível definir o capitão.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };

    const formatarMoeda = (valor) => {
        if (valor === null || valor === undefined) return 'R$ 0,00';
        return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
    };

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

    const AbaPremiacoes = () => {
        const minhasPremiacoes = premiacoes.filter(p => 
            atletas.some(a => a.id === p.jogadorId)
        );

        return (
            <div className="atleta-list-container">
                <div className="atleta-list-item header" style={{gridTemplateColumns: '2fr 2fr 1.5fr 1fr'}}>
                    <span>Prémio</span>
                    <span>Jogador Vencedor</span>
                    <span>Valor (Bônus)</span>
                    <span>Data</span>
                </div>
                
                {minhasPremiacoes.length === 0 ? (
                    <div style={{padding:20, textAlign:'center', color:'#777'}}>Nenhuma premiação registrada para seus atletas.</div>
                ) : (
                    minhasPremiacoes.map(p => {
                        const jogador = atletas.find(a => a.id === p.jogadorId);
                        const nomeJogador = jogador ? jogador.nome : `ID: ${p.jogadorId}`;
                        
                        return (
                            <div key={p.id} className="atleta-list-item" style={{gridTemplateColumns: '2fr 2fr 1.5fr 1fr'}}>
                                <span style={{fontWeight:'bold', color:'#004d40'}}>
                                    <FaTrophy style={{marginRight:8, color:'#f0ad4e'}}/> {p.nome}
                                </span>
                                <span>{nomeJogador}</span>
                                <span style={{color: '#2e7d32', fontWeight:'bold'}}>
                                    <FaMoneyBillWave style={{marginRight:5}}/>
                                    {formatarMoeda(p.valor)}
                                </span>
                                <span>{formatarDataSemFuso(p.dataPremiacao)}</span>
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
                <h2>Gestão de Competições</h2>
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
                
                {activeTab === 'atletas' && (
                     <button className="registrar-inscricao-btn" onClick={() => setIsModalInscricaoOpen(true)} disabled={!competicaoSelecionada}>
                        + Registrar Inscrição
                    </button>
                )}
                {activeTab === 'premiacoes' && (
                     <button className="registrar-inscricao-btn" onClick={() => setIsModalPremiacaoOpen(true)}>
                        + Gerar Premiação do Mês
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

            {isModalPremiacaoOpen && (
                <RegistrarPremiacaoModal
                    onClose={() => setIsModalPremiacaoOpen(false)}
                    onSuccess={handleRecarregarPremiacoes}
                />
            )}
        </div>
    );
};