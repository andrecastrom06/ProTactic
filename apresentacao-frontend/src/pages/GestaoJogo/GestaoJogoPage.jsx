import React, { useState, useRef, useEffect } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';
import { AbaAtribuirNotas } from './components/AbaAtribuirNotas';
import { AbaInformacoesJogo } from './components/AbaInformacoesJogo'; // <--- NOVO IMPORT
import { CriarPartidaModal } from '../../components/CriarPartidaModal'; 
import './GestaoJogoPage.css';

import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { buscarJogosDoClube, salvarEscalacao, buscarEscalacaoPorPartida } from '../../services/jogoService'; 
import { atribuirNota, buscarNotasPorJogo } from '../../services/notaService';

export const GestaoJogoPage = () => {
    const { clubeIdLogado } = useAuth();
    
    // Abas: 'ESCALACAO', 'NOTAS', 'INFO_JOGO'
    const [abaAtiva, setAbaAtiva] = useState('ESCALACAO');

    // --- Estados de Dados ---
    const [todosAtletas, setTodosAtletas] = useState([]); 
    const [atletasDisponiveis, setAtletasDisponiveis] = useState([]);
    const [reservas, setReservas] = useState([]); 
    const [atletasNoCampo, setAtletasNoCampo] = useState([]);
    
    const [partidas, setPartidas] = useState([]);
    const [partidaSelecionada, setPartidaSelecionada] = useState('');
    
    const [escalacaoJaExiste, setEscalacaoJaExiste] = useState(false);
    const [notasDoBanco, setNotasDoBanco] = useState([]);
    
    // Estado para as estatísticas (Gols, Assist, Cartões)
    const [estatisticasJogo, setEstatisticasJogo] = useState({});

    const [esquemaTatico, setEsquemaTatico] = useState('4-3-3'); 
    const [showModalCriar, setShowModalCriar] = useState(false);
    const [loading, setLoading] = useState(true);
    const campoTaticoRef = useRef(null);

    // 1. Carrega Dados Iniciais
    useEffect(() => {
        const carregarDados = async () => {
            if (!clubeIdLogado) return;
            setLoading(true);
            try {
                const players = await buscarTodosJogadores();
                const meusJogadores = players.filter(j => 
                    j.clubeId === clubeIdLogado && j.saudavel && j.contratoAtivo
                ).map(j => ({ ...j, numero: j.id, posicao: j.posicao || '?' }));
                
                setTodosAtletas(meusJogadores);
                setAtletasDisponiveis(meusJogadores);

                const jogos = await buscarJogosDoClube(clubeIdLogado);
                setPartidas(jogos);
                
                if (jogos.length > 0) {
                    setPartidaSelecionada(String(jogos[0].id));
                }
            } catch (error) {
                console.error("Erro inicial:", error);
            } finally {
                setLoading(false);
            }
        };
        carregarDados();
    }, [clubeIdLogado]);

    // 2. Carrega Detalhes da Partida Selecionada
    useEffect(() => {
        const carregarDetalhesPartida = async () => {
            if (!partidaSelecionada || todosAtletas.length === 0 || !clubeIdLogado) return;

            try {
                // Reset visual
                setAtletasNoCampo([]);
                setReservas([]);
                setEscalacaoJaExiste(false);
                setNotasDoBanco([]); 
                setEstatisticasJogo({}); // Reset stats

                let poolDeAtletas = [...todosAtletas];

                // A. Busca Escalação
                const escalacao = await buscarEscalacaoPorPartida(partidaSelecionada, clubeIdLogado);
                
                if (escalacao) {
                    setEscalacaoJaExiste(true); 
                    setEsquemaTatico(escalacao.esquema || '4-3-3');
                    
                    const idsTitulares = [
                        escalacao.idJogador1, escalacao.idJogador2, escalacao.idJogador3,
                        escalacao.idJogador4, escalacao.idJogador5, escalacao.idJogador6,
                        escalacao.idJogador7, escalacao.idJogador8, escalacao.idJogador9,
                        escalacao.idJogador10, escalacao.idJogador11
                    ].filter(id => id !== null); 

                    const novosTitulares = [];
                    idsTitulares.forEach(id => {
                        const index = poolDeAtletas.findIndex(a => a.id === id);
                        if (index !== -1) {
                            const atleta = poolDeAtletas[index];
                            novosTitulares.push({ ...atleta, position: { x: 50, y: 50 } });
                            poolDeAtletas.splice(index, 1); 
                        }
                    });
                    setAtletasNoCampo(novosTitulares);
                    setAtletasDisponiveis(poolDeAtletas); 
                } else {
                    setAtletasDisponiveis(todosAtletas);
                }

                // B. Busca Notas
                const notas = await buscarNotasPorJogo("JOGO-" + partidaSelecionada);
                setNotasDoBanco(notas);

                // C. Busca Estatísticas (Simulação via LocalStorage por enquanto)
                // Futuramente: await buscarEstatisticas(partidaSelecionada);
                const statsSalvas = localStorage.getItem(`stats_jogo_${partidaSelecionada}`);
                if (statsSalvas) {
                    setEstatisticasJogo(JSON.parse(statsSalvas));
                }

            } catch (error) {
                console.error("Erro ao carregar detalhes:", error);
            }
        };
        carregarDetalhesPartida();
    }, [partidaSelecionada, todosAtletas, clubeIdLogado]);

    // 3. Drag & Drop Logic (Mantida igual)
    const handleDragEnd = (event) => {
        const { active, over, delta } = event;
        if (!over) return; 

        const atletaArrastado = active.data.current.atleta;
        const tipoOrigem = active.data.current.type; 
        const idDestino = over.id; 
        const campoRect = campoTaticoRef.current?.getBoundingClientRect();

        if (tipoOrigem === 'NO_CAMPO' && idDestino === 'CAMPO_TATICO') {
            if (!campoRect) return;
            const percentDeltaX = (delta.x / campoRect.width) * 100;
            const percentDeltaY = (delta.y / campoRect.height) * 100;
            setAtletasNoCampo(items => items.map(item =>
                item.id === active.id ? { ...item, position: { x: item.position.x + percentDeltaX, y: item.position.y + percentDeltaY } } : item
            ));
            return;
        }
        
        if (tipoOrigem === 'DISPONIVEL') setAtletasDisponiveis(p => p.filter(a => a.id !== atletaArrastado.id));
        else if (tipoOrigem === 'RESERVA') setReservas(p => p.filter(a => a.id !== atletaArrastado.id));
        else if (tipoOrigem === 'NO_CAMPO') setAtletasNoCampo(p => p.filter(a => a.id !== atletaArrastado.id));

        const { position, ...atletaLimpo } = atletaArrastado;

        if (idDestino === 'CAMPO_TATICO') {
            let newPosition = { x: 50, y: 50 }; 
            if (campoRect && event.active.rect.current.translated) {
                 const itemRect = event.active.rect.current.translated;
                 let relativeX = itemRect.left - campoRect.left - (itemRect.width / 2);
                 let relativeY = itemRect.top - campoRect.top - (itemRect.height / 2);
                 newPosition = { 
                     x: (relativeX / campoRect.width) * 100, 
                     y: (relativeY / campoRect.height) * 100 
                 };
            }
            if (atletasNoCampo.length >= 11) alert("Atenção: Já existem 11 jogadores em campo.");
            setAtletasNoCampo(prev => [...prev, { ...atletaLimpo, position: newPosition }]);
        } else if (idDestino === 'DISPONIVEIS_LIST') {
            setAtletasDisponiveis(p => [...p, atletaLimpo]);
        } else if (idDestino === 'RESERVAS_LIST') {
            setReservas(p => [...p, atletaLimpo]);
        }
    };

    // 4. Handlers de Salvar
    const handleSalvarEscalacao = async () => {
        if (!partidaSelecionada) return alert("Selecione uma partida.");
        
        if (escalacaoJaExiste) {
            if (!window.confirm("Substituir escalação existente?")) return;
        }

        const idsJogadores = atletasNoCampo.map(a => a.id);
        const payload = {
            partidaId: parseInt(partidaSelecionada, 10),
            esquema: esquemaTatico,
            jogadoresIds: idsJogadores,
            clubeId: parseInt(clubeIdLogado, 10) 
        };

        try {
            await salvarEscalacao(payload);
            alert("Escalação salva!");
            setEscalacaoJaExiste(true); 
        } catch (err) {
            alert("Erro: " + err.message);
        }
    };

    const handleSalvarNotas = async (avaliacoes) => {
        if (!partidaSelecionada) return;
        try {
            const promises = Object.entries(avaliacoes).map(([jogadorIdStr, dados]) => {
                if (dados.nota > 0 || dados.observacao) {
                    return atribuirNota({
                        jogoId: "JOGO-" + partidaSelecionada,
                        jogadorId: jogadorIdStr,
                        nota: dados.nota || 0,
                        observacao: dados.observacao || ""
                    });
                }
                return Promise.resolve();
            });
            await Promise.all(promises);
            alert("Notas salvas!");
        } catch (error) {
            alert("Erro: " + error.message);
        }
    };

    const handleSalvarInfoJogo = async (novasEstatisticas) => {
        if (!partidaSelecionada) return;
        // Aqui você chamaria o backend para salvar Gols/Assistencias/Cartoes
        // Como não temos o endpoint pronto, salvamos no localStorage para persistir na sessão do navegador
        localStorage.setItem(`stats_jogo_${partidaSelecionada}`, JSON.stringify(novasEstatisticas));
        setEstatisticasJogo(novasEstatisticas);
        alert("Informações do jogo salvas com sucesso!");
    };

    // Lista combinada de atletas escalados (Campo + Banco)
    const atletasDoJogo = [...atletasNoCampo, ...reservas]; 
    const partidaObj = partidas.find(p => p.id === Number(partidaSelecionada));
    const tituloPartida = partidaObj ? `Jogo: ${partidaObj.id}` : "Selecione";

    if (loading) return <div style={{padding:20}}>Carregando...</div>;

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                <header className="gestao-jogo-header">
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <h2>{tituloPartida}</h2>
                    </div>
                    <nav className="gestao-jogo-tabs">
                        <span className={`tab-item ${abaAtiva === 'ESCALACAO' ? 'active' : ''}`} 
                              onClick={() => setAbaAtiva('ESCALACAO')}>
                            Escalação
                        </span>
                        <span className={`tab-item ${abaAtiva === 'NOTAS' ? 'active' : ''}`} 
                              onClick={() => setAbaAtiva('NOTAS')}>
                            Atribuir Notas
                        </span>
                        <span className={`tab-item ${abaAtiva === 'INFO_JOGO' ? 'active' : ''}`} 
                              onClick={() => setAbaAtiva('INFO_JOGO')}>
                            Informações do Jogo
                        </span>
                    </nav>
                </header>
                
                <div className="gestao-jogo-content">
                    
                    {/* === ABA 1: ESCALAÇÃO === */}
                    {abaAtiva === 'ESCALACAO' && (
                        <>
                            <div className="campo-column">
                                <div className="campo-header-controls" style={{display:'flex', justifyContent:'space-between', marginBottom: '10px'}}>
                                    <span>Defina os titulares no campo.</span>
                                    <button onClick={handleSalvarEscalacao} className="btn-salvar-escalacao">💾 Salvar</button>
                                </div>
                                <CampoTatico atletas={atletasNoCampo} fieldRef={campoTaticoRef} />
                            </div>
                            <div className="lista-column">
                                <div className="partida-selector-box" style={{marginBottom:'15px', padding:'10px', background:'#eef'}}>
                                    <label style={{display:'block', marginBottom:'5px', fontWeight:'bold'}}>Selecionar Partida:</label>
                                    <select value={partidaSelecionada} onChange={(e) => setPartidaSelecionada(e.target.value)} style={{width:'100%', marginBottom:'5px'}}>
                                        <option value="">Selecione...</option>
                                        {partidas.map(p => <option key={p.id} value={p.id}>Jogo #{p.id}</option>)}
                                    </select>
                                    <button className="btn-novo-jogo" onClick={() => setShowModalCriar(true)} style={{width:'100%'}}>+ Criar Nova</button>
                                </div>
                                <ListaAtletas disponiveis={atletasDisponiveis} reservas={reservas} />
                            </div>
                        </>
                    )}

                    {/* === ABA 2: ATRIBUIR NOTAS === */}
                    {abaAtiva === 'NOTAS' && (
                        <div style={{width:'100%', padding:'0 20px'}}>
                            {atletasDoJogo.length === 0 ? (
                                <p style={{textAlign:'center', marginTop:40}}>Defina a escalação primeiro.</p>
                            ) : (
                                <AbaAtribuirNotas 
                                    atletas={atletasDoJogo}
                                    notasIniciais={notasDoBanco} 
                                    onSalvar={handleSalvarNotas} 
                                />
                            )}
                        </div>
                    )}

                    {/* === ABA 3: INFORMAÇÕES DO JOGO (NOVA) === */}
                    {abaAtiva === 'INFO_JOGO' && (
                        <div style={{width:'100%', padding:'0 20px'}}>
                            {atletasDoJogo.length === 0 ? (
                                <p style={{textAlign:'center', marginTop:40}}>Defina a escalação primeiro para preencher as estatísticas.</p>
                            ) : (
                                <AbaInformacoesJogo 
                                    atletas={atletasDoJogo}
                                    dadosIniciais={estatisticasJogo}
                                    onSalvar={handleSalvarInfoJogo}
                                />
                            )}
                        </div>
                    )}

                </div>
            </div>

            {showModalCriar && (
                <CriarPartidaModal 
                    onClose={() => setShowModalCriar(false)} 
                    onSuccess={(nova) => {
                        setPartidas([...partidas, nova]);
                        setPartidaSelecionada(String(nova.id));
                    }} 
                    clubeIdLogado={clubeIdLogado}
                />
            )}
        </DndContext>
    );
};