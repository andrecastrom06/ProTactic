import React, { useState, useEffect, useCallback } from 'react';
import { 
    LuCalendarDays, LuUsers, LuDumbbell,
    LuX, LuCheck, LuZap, LuUser, LuRefreshCw
} from "react-icons/lu";
import { GiSoccerField, GiWhistle } from "react-icons/gi";
import { FaPlus } from "react-icons/fa";
import './TreinosPage.css';

import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { buscarJogosDoClube, buscarEscalacaoDaPartida } from '../../services/jogoService';
import { buscarTodosClubes } from '../../services/clubeService'; 
import { 
    buscarTodasSessoesTaticas, 
    criarSessaoTatica, 
    salvarTreinoFisico, 
    buscarTreinosFisicosPorJogador,
    atualizarStatusTreinoFisico
} from '../../services/treinoService';

export const TreinosPage = () => {
    const { clubeIdLogado } = useAuth();
    
    const [jogadores, setJogadores] = useState([]); 
    const [jogadoresEscalados, setJogadoresEscalados] = useState([]); 
    const [partidas, setPartidas] = useState([]);
    const [clubes, setClubes] = useState([]); 
    
    const [listaTaticos, setListaTaticos] = useState([]);
    const [listaFisicos, setListaFisicos] = useState([]);
    const [loading, setLoading] = useState(true);

    const [modalAberto, setModalAberto] = useState(false);
    const [tipoTreinoSelecionado, setTipoTreinoSelecionado] = useState(''); 
    const [loadingStatus, setLoadingStatus] = useState(null);

    const [novoTreino, setNovoTreino] = useState({
        nome: '', intensidade: 50, partidaId: '', 
        escopoTatico: 'EQUIPE', jogadorTaticoId: '',
        jogadorFisicoId: '', dataInicio: '', dataFim: '', musculo: ''
    });

    const formatarData = (dataString) => {
        if (!dataString) return 'Data Indef.';
        const date = new Date(dataString);
        return date.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
    };

    const resolverNomeAdversario = useCallback((partida, listaDeClubes) => {
        if (!partida || !listaDeClubes.length || !clubeIdLogado) return 'Adversário';
        const idMeuClube = parseInt(clubeIdLogado);
        const idCasa = parseInt(partida.clubeCasaId); 
        const idVisitante = parseInt(partida.clubeVisitanteId); 
        const idAdversario = (idCasa === idMeuClube) ? idVisitante : idCasa;
        const clubeAdv = listaDeClubes.find(c => c.id === idAdversario);
        return clubeAdv ? clubeAdv.nome : `Time #${idAdversario}`;
    }, [clubeIdLogado]);

    const carregarDados = useCallback(async () => {
        if (!clubeIdLogado) return;
        try {
            const [todosJogadores, todosJogos, sessoesTaticas, listaClubes] = await Promise.all([
                buscarTodosJogadores(),
                buscarJogosDoClube(clubeIdLogado),
                buscarTodasSessoesTaticas(),
                buscarTodosClubes()
            ]);

            const meusJogadores = todosJogadores.filter(j => j.clubeId === parseInt(clubeIdLogado));
            setJogadores(meusJogadores);
            setPartidas(todosJogos);
            setClubes(listaClubes);

            const taticosFormatados = sessoesTaticas.map(s => {
                const jogo = todosJogos.find(p => p.id === s.partidaId);
                const nomeAdversario = jogo ? resolverNomeAdversario(jogo, listaClubes) : 'Partida não encontrada';
                const dataFormatada = jogo ? formatarData(jogo.dataJogo) : '--/--';
                return {
                    id: s.id, nome: s.nome, partidaId: s.partidaId,
                    adversario: nomeAdversario, data: dataFormatada,
                    escopo: (s.convocadosIds && s.convocadosIds.length > 0 && s.convocadosIds.length < meusJogadores.length) ? 'INDIVIDUAL' : 'EQUIPE',
                };
            });
            setListaTaticos(taticosFormatados);

            let acumuladorFisicos = [];
            await Promise.all(meusJogadores.map(async (j) => {
                const treinosDoAtleta = await buscarTreinosFisicosPorJogador(j.id);
                const formatados = treinosDoAtleta.map(t => ({
                    id: t.id, nomeAtleta: j.nome, foco: t.nome, musculo: t.musculo,
                    dataInicio: formatarData(t.dataInicio), dataFim: formatarData(t.dataFim),
                    intensidadeValor: t.intensidade === 'Alta' ? 85 : (t.intensidade === 'Media' ? 50 : 20),
                    intensidadeLabel: t.intensidade,
                    status: t.status || 'Planejado'
                }));
                acumuladorFisicos = [...acumuladorFisicos, ...formatados];
            }));
            setListaFisicos(acumuladorFisicos);

        } catch (error) {
            console.error("Erro dados:", error);
        } finally {
            setLoading(false);
        }
    }, [clubeIdLogado, resolverNomeAdversario]); 

    useEffect(() => {
        setLoading(true);
        carregarDados();
    }, [carregarDados]);

    const handlePartidaChange = async (e) => {
        const idPartida = e.target.value; 
        setNovoTreino(prev => ({ ...prev, partidaId: idPartida }));
        setJogadoresEscalados([]); 

        if (idPartida && novoTreino.escopoTatico === 'INDIVIDUAL') {
            try {
                const resposta = await buscarEscalacaoDaPartida(idPartida, clubeIdLogado);
                if (resposta) {
                    const nomesEscalados = Array.isArray(resposta) ? resposta : [];
                    const escalados = jogadores.filter(j => nomesEscalados.includes(j.nome));
                    setJogadoresEscalados(escalados);
                }
            } catch (err) {
                console.error("Erro escalação:", err);
            }
        }
    };

    const handleAtualizarStatusFisico = async (id, statusAtual) => {
        setLoadingStatus(id); 
        let novoStatus = 'Planejado';
        if (statusAtual === 'Planejado') novoStatus = 'Em Andamento';
        else if (statusAtual === 'Em Andamento') novoStatus = 'Concluído';
        else if (statusAtual === 'Concluído') novoStatus = 'Planejado'; 
        
        try {
            await atualizarStatusTreinoFisico(id, novoStatus);
            setListaFisicos(prev => prev.map(t => t.id === id ? { ...t, status: novoStatus } : t));
        } catch (err) {
            alert("Erro ao atualizar status: " + err.message);
        } finally {
            setLoadingStatus(null);
        }
    };

    const abrirModal = (tipo) => {
        setTipoTreinoSelecionado(tipo);
        setModalAberto(true);
        setNovoTreino({ 
            nome: '', intensidade: 50, partidaId: '', 
            escopoTatico: 'EQUIPE', jogadorTaticoId: '',
            jogadorFisicoId: '', dataInicio: '', dataFim: '', musculo: ''
        });
        setJogadoresEscalados([]);
    };

    const fecharModal = () => setModalAberto(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNovoTreino(prev => ({ ...prev, [name]: value }));
    };

    const handleSalvarTreino = async (e) => {
        e.preventDefault();
        try {
            if (tipoTreinoSelecionado === 'Físico') {
                if (!novoTreino.jogadorFisicoId) return alert("Selecione um atleta.");
                
                let intensidadeEnum = 'Baixa';
                if (novoTreino.intensidade > 66) intensidadeEnum = 'Alta';
                else if (novoTreino.intensidade > 33) intensidadeEnum = 'Media';

                const payload = {
                    nome: novoTreino.nome,
                    musculo: novoTreino.musculo || 'Geral',
                    intensidade: intensidadeEnum,
                    descricao: `Intensidade: ${novoTreino.intensidade}%`,
                    dataInicio: novoTreino.dataInicio,
                    dataFim: novoTreino.dataFim
                };

                await salvarTreinoFisico(novoTreino.jogadorFisicoId, payload);
                alert("Treino Físico registrado!");
            } 
            else if (tipoTreinoSelecionado === 'Tático') {
                if (!novoTreino.partidaId) return alert("Selecione uma partida.");
                
                let listaConvocados = [];
                if (novoTreino.escopoTatico === 'INDIVIDUAL') {
                    if(!novoTreino.jogadorTaticoId) return alert("Selecione o jogador escalado.");
                    listaConvocados = [parseInt(novoTreino.jogadorTaticoId)];
                } else {
                    listaConvocados = jogadores.map(j => j.id);
                }
                
                const payload = {
                    nome: novoTreino.nome,
                    partidaId: parseInt(novoTreino.partidaId, 10),
                    convocadosIds: listaConvocados,
                    clubeId: parseInt(clubeIdLogado, 10)
                };

                await criarSessaoTatica(payload);
                alert("Sessão Tática agendada!");
            }
            fecharModal();
            carregarDados();
        } catch (err) {
            alert("Erro: " + err.message);
        }
    };

    const getStatusColor = (status) => {
        if(status === 'Concluído') return 'status-success';
        if(status === 'Em Andamento') return 'status-warning';
        return 'status-gray';
    };

    return (
        <div className="treinos-page">
            <header className="treinos-header">
                <div>
                    <h1>Gestão de Treinos</h1>
                    <p>Painel técnico e físico</p>
                </div>
            </header>

            {/* SEÇÃO 1: TÁTICO */}
            <section className="section-container">
                <div className="section-header tatico-border">
                    <div className="header-title-group">
                        <GiSoccerField size={24} color="#009688"/>
                        <div>
                            <h2>Sessões Táticas</h2>
                            <small>Focadas na Partida</small>
                        </div>
                    </div>
                    <button className="btn-action outline" onClick={() => abrirModal('Tático')}>
                        <FaPlus /> Nova Sessão
                    </button>
                </div>

                <div className="cards-list">
                    {loading && listaTaticos.length === 0 ? <p>Carregando...</p> : listaTaticos.length === 0 ? <p className="empty-msg">Nenhuma sessão agendada.</p> : 
                    listaTaticos.map(treino => (
                        <div key={treino.id} className="training-card tatico">
                            <div className="card-left">
                                <div className="card-header-row">
                                    <span className="match-badge">vs {treino.adversario}</span>
                                    {treino.escopo === 'INDIVIDUAL' ? 
                                        <span className="scope-badge individual"><LuUser size={12}/> Individual</span> :
                                        <span className="scope-badge team"><LuUsers size={12}/> Equipe</span>
                                    }
                                </div>
                                <h3>{treino.nome}</h3>
                                <div className="card-meta">
                                    <span className="meta-tag"><LuCalendarDays size={12}/> {treino.data}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </section>

            {/* SEÇÃO 2: FÍSICO */}
            <section className="section-container">
                <div className="section-header fisico-border">
                    <div className="header-title-group">
                        <LuDumbbell size={24} color="#4caf50"/>
                        <div>
                            <h2>Treinos Físicos</h2>
                            <small>Monitoramento de Carga</small>
                        </div>
                    </div>
                    <button className="btn-action primary" onClick={() => abrirModal('Físico')}>
                        <FaPlus /> Novo Treino
                    </button>
                </div>

                <div className="cards-list">
                    {loading && listaFisicos.length === 0 ? <p>Carregando...</p> : listaFisicos.length === 0 ? <p className="empty-msg">Nenhum treino físico.</p> :
                    listaFisicos.map(treino => (
                        <div key={treino.id} className="training-card fisico">
                            <div className="card-left">
                                <div className="player-row">
                                    <span className="player-avatar">{treino.nomeAtleta ? treino.nomeAtleta.charAt(0) : '?'}</span>
                                    <strong>{treino.nomeAtleta}</strong>
                                </div>
                                <h3 className="focus-title">{treino.foco} <small>({treino.musculo})</small></h3>
                                <div className="card-meta">
                                    <span><LuCalendarDays size={12}/> {treino.dataInicio} - {treino.dataFim}</span>
                                </div>
                                
                                <div className="intensity-mini-bar">
                                    <div style={{width: `${treino.intensidadeValor}%`, background: treino.intensidadeValor > 80 ? '#ef4444' : '#4caf50'}}></div>
                                </div>
                                <small style={{color:'#666', fontSize:'0.7rem'}}>Intensidade: {treino.intensidadeLabel}</small>
                            </div>
                            <div className="card-right">
                                <span className={`status-pill ${getStatusColor(treino.status)}`}>{treino.status}</span>
                                <button 
                                    className="btn-update-status" 
                                    onClick={() => handleAtualizarStatusFisico(treino.id, treino.status)}
                                    disabled={loadingStatus === treino.id}
                                    title="Mudar Status"
                                >
                                    <LuRefreshCw className={loadingStatus === treino.id ? 'spinning' : ''}/>
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </section>

            {/* MODAL */}
            {modalAberto && (
                <div className="modal-overlay" onClick={(e) => { if(e.target.className === 'modal-overlay') fecharModal() }}>
                    <div className="modal-content">
                        <div className={`modal-header ${tipoTreinoSelecionado === 'Físico' ? 'fisico-header' : 'tatico-header'}`}>
                            <div className="header-icon-title">
                                {tipoTreinoSelecionado === 'Físico' ? <LuDumbbell size={24}/> : <GiWhistle size={24}/>}
                                <div>
                                    <h3>Novo Treino {tipoTreinoSelecionado}</h3>
                                </div>
                            </div>
                            <button onClick={fecharModal} className="btn-close"><LuX /></button>
                        </div>

                        <form onSubmit={handleSalvarTreino} className="modal-form">
                            
                            {tipoTreinoSelecionado === 'Tático' && (
                                <>
                                    <label>Escopo</label>
                                    <div className="scope-toggle">
                                        <button 
                                            type="button"
                                            className={`scope-btn ${novoTreino.escopoTatico === 'EQUIPE' ? 'active' : ''}`}
                                            onClick={() => setNovoTreino({...novoTreino, escopoTatico: 'EQUIPE', jogadorTaticoId: ''})}
                                        >
                                            <LuUsers style={{verticalAlign:'middle'}}/> Equipe Toda
                                        </button>
                                        <button 
                                            type="button"
                                            className={`scope-btn ${novoTreino.escopoTatico === 'INDIVIDUAL' ? 'active' : ''}`}
                                            onClick={() => setNovoTreino({...novoTreino, escopoTatico: 'INDIVIDUAL'})}
                                        >
                                            <LuUser style={{verticalAlign:'middle'}}/> Individual
                                        </button>
                                    </div>

                                    <div className="form-section">
                                        <label>Partida (Obrigatório)</label>
                                        <div className="select-wrapper">
                                            <GiSoccerField className="input-icon" />
                                            <select 
                                                name="partidaId" 
                                                value={novoTreino.partidaId} 
                                                onChange={(e) => { handleInputChange(e); handlePartidaChange(e); }} 
                                                required
                                            >
                                                <option value="">Selecione o jogo...</option>
                                                {partidas.map(p => (
                                                    <option key={p.id} value={p.id}>
                                                        vs {resolverNomeAdversario(p, clubes)} ({formatarData(p.dataJogo)})
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <div className="form-section">
                                        <label>Nome da Sessão</label>
                                        <input type="text" name="nome" value={novoTreino.nome} onChange={handleInputChange} placeholder="Ex: Tático Defensivo" required />
                                    </div>

                                    {novoTreino.escopoTatico === 'INDIVIDUAL' && (
                                        <div className="form-section animated-fade">
                                            <label>Jogador Convocado</label>
                                            <div className="select-wrapper">
                                                <LuUser className="input-icon" />
                                                <select name="jogadorTaticoId" value={novoTreino.jogadorTaticoId} onChange={handleInputChange} required>
                                                    <option value="">Selecione...</option>
                                                    {jogadoresEscalados.length > 0 ? (
                                                        jogadoresEscalados.map(j => (
                                                            <option key={j.id} value={j.id}>{j.nome} ({j.posicao})</option>
                                                        ))
                                                    ) : (
                                                        <option value="" disabled>Nenhum jogador escalado ou partida não selecionada</option>
                                                    )}
                                                </select>
                                            </div>
                                        </div>
                                    )}
                                </>
                            )}

                            {tipoTreinoSelecionado === 'Físico' && (
                                <>
                                    <div className="form-section">
                                        <label>Atleta</label>
                                        <div className="select-wrapper">
                                            <LuUsers className="input-icon" />
                                            <select name="jogadorFisicoId" value={novoTreino.jogadorFisicoId} onChange={handleInputChange} required>
                                                <option value="">Selecione...</option>
                                                {jogadores.map(j => (
                                                    <option key={j.id} value={j.id}>{j.nome} ({j.posicao})</option>
                                                ))}
                                            </select>
                                        </div>
                                    </div>
                                    <div className="form-section">
                                        <label>Nome do Treino</label>
                                        <input type="text" name="nome" value={novoTreino.nome} onChange={handleInputChange} placeholder="Ex: Recuperação" required />
                                    </div>
                                    <div className="form-section">
                                        <label>Músculo / Foco</label>
                                        <input type="text" name="musculo" value={novoTreino.musculo} onChange={handleInputChange} placeholder="Ex: Pernas" required />
                                    </div>
                                    <div className="form-row">
                                        <div className="form-section">
                                            <label>Início</label>
                                            <input type="date" name="dataInicio" value={novoTreino.dataInicio} onChange={handleInputChange} required />
                                        </div>
                                        <div className="form-section">
                                            <label>Fim</label>
                                            <input type="date" name="dataFim" value={novoTreino.dataFim} onChange={handleInputChange} required />
                                        </div>
                                    </div>
                                    <div className="form-section intensity-section">
                                        <div className="intensity-header">
                                            <label><LuZap size={14}/> Intensidade</label>
                                            <span className="intensity-value">{novoTreino.intensidade}%</span>
                                        </div>
                                        <input type="range" name="intensidade" min="0" max="100" className="range-slider fisico" value={novoTreino.intensidade} onChange={handleInputChange} />
                                    </div>
                                </>
                            )}

                            <div className="modal-footer">
                                <button type="button" onClick={fecharModal} className="btn-cancel">Cancelar</button>
                                <button type="submit" className={`btn-save ${tipoTreinoSelecionado.toLowerCase()}`}>
                                    <LuCheck /> Salvar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};