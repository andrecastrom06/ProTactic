import React, { useState, useRef, useEffect } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

// Componentes existentes
import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';
import { AbaAtribuirNotas } from './components/AbaAtribuirNotas';
import { CriarPartidaModal } from '../../components/CriarPartidaModal'; 
// NOVOS COMPONENTES
import { AbaCartoesInfo } from './components/AbaCartoesInfo';
import { AbaSuspensoes } from './components/AbaSuspensoes';

import './GestaoJogoPage.css';
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { buscarJogosDoClube, salvarEscalacao, buscarEscalacaoPorPartida } from '../../services/jogoService'; 
import { atribuirNota, buscarNotasPorJogo } from '../../services/notaService';

export const GestaoJogoPage = () => {
    const { clubeIdLogado } = useAuth();
    
    const [abaAtiva, setAbaAtiva] = useState('ESCALACAO'); 

    // --- Estados de Dados ---
    const [todosAtletas, setTodosAtletas] = useState([]); 
    const [atletasDisponiveis, setAtletasDisponiveis] = useState([]);
    const [reservas, setReservas] = useState([]); 
    const [atletasNoCampo, setAtletasNoCampo] = useState([]);
    
    const [partidas, setPartidas] = useState([]);
    const [partidaSelecionada, setPartidaSelecionada] = useState('');
    
    // Este estado controla se a mensagem deve aparecer ou não
    const [escalacaoJaExiste, setEscalacaoJaExiste] = useState(false);
    const [notasDoBanco, setNotasDoBanco] = useState([]);

    const [esquemaTatico, setEsquemaTatico] = useState('4-3-3'); 
    const [showModalCriar, setShowModalCriar] = useState(false);
    const [loading, setLoading] = useState(true);
    const campoTaticoRef = useRef(null);

    // Carregamento inicial de dados
    useEffect(() => {
        const carregarDados = async () => {
            if (!clubeIdLogado) return;
            setLoading(true);
            try {
                const players = await buscarTodosJogadores();
                const meusJogadores = players.filter(j => j.clubeId === parseInt(clubeIdLogado));
                
                const mappedJogadores = meusJogadores.map(j => ({ 
                    ...j, 
                    numero: j.id, 
                    posicao: j.posicao || '?',
                    suspenso: j.status === 'Suspenso', 
                    cartoesAmarelos: 0, 
                    cartoesVermelhos: 0
                }));
                
                setTodosAtletas(mappedJogadores);
                setAtletasDisponiveis(mappedJogadores.filter(j => j.saudavel && j.contratoAtivo && !j.suspenso));

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

    // Carrega Detalhes da Partida
    useEffect(() => {
        const carregarDetalhesPartida = async () => {
            if (!partidaSelecionada || todosAtletas.length === 0 || !clubeIdLogado) return;

            try {
                setAtletasNoCampo([]);
                setReservas([]);
                setEscalacaoJaExiste(false); // Resetamos antes de verificar
                setNotasDoBanco([]); 

                let poolDeAtletas = [...todosAtletas.filter(j => j.saudavel && j.contratoAtivo && !j.suspenso)];

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
                    setAtletasDisponiveis(poolDeAtletas);
                }

                const notas = await buscarNotasPorJogo("JOGO-" + partidaSelecionada);
                setNotasDoBanco(notas);

            } catch (error) {
                console.error("Erro ao carregar detalhes:", error);
            }
        };
        carregarDetalhesPartida();
    }, [partidaSelecionada, todosAtletas, clubeIdLogado]);

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

    const handleSalvarEscalacao = async () => {
        if (!partidaSelecionada) {
            alert("Selecione uma partida primeiro.");
            return;
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
            alert("Escalação salva com sucesso!");
            setEscalacaoJaExiste(true); 
        } catch (err) {
            alert("Erro ao salvar: " + err.message);
        }
    };
    const handleSalvarNotas = async (avaliacoes) => {
        if (!partidaSelecionada) {
            alert("Selecione uma partida primeiro.");
            return;
        }

        setLoading(true);
        try {
            const promessas = Object.keys(avaliacoes).map(async (atletaId) => {
                const dados = avaliacoes[atletaId];
                
                // Envia se tiver nota ou observação
                if (dados.nota > 0 || (dados.observacao && dados.observacao.trim() !== '')) {
                    const payload = {
                        jogoId: `JOGO-${partidaSelecionada}`, 
                        jogadorId: String(atletaId),
                        nota: parseFloat(dados.nota),
                        observacao: dados.observacao
                    };
                    return atribuirNota(payload);
                }
                return null;
            });

            await Promise.all(promessas);
            
         
            const notasAtualizadas = await buscarNotasPorJogo("JOGO-" + partidaSelecionada);
            setNotasDoBanco(notasAtualizadas);

            alert("Notas e observações salvas e atualizadas com sucesso!");

        } catch (error) {
            console.error("Erro ao salvar notas:", error);
            alert("Erro ao salvar notas: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    const atletasRelacionados = [...atletasNoCampo, ...reservas]; 
    
    const AvisoSemEscalacao = () => (
        <div style={{
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center', 
            justifyContent: 'center', 
            padding: '40px',
            backgroundColor: '#f8d7da',
            color: '#721c24',
            borderRadius: '8px',
            border: '1px solid #f5c6cb',
            marginTop: '20px'
        }}>
            <h3>⚠️ Escalação Pendente</h3>
            <p>A partida ainda não tem uma escalação definida.</p>
            <p style={{fontSize: '0.9em'}}>Por favor, aceda à aba "Escalação Tática" e salve os titulares antes de atribuir notas ou gerir cartões.</p>
        </div>
    );

    if (loading) return <div style={{padding:20}}>Carregando dados...</div>;

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                <header className="gestao-jogo-header">
                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <h2>Gestão da Partida #{partidaSelecionada}</h2>
                    </div>
                    <nav className="gestao-jogo-tabs">
                        <span className={`tab-item ${abaAtiva === 'ESCALACAO' ? 'active' : ''}`} onClick={() => setAbaAtiva('ESCALACAO')}>
                            Escalação Tática
                        </span>
                        <span className={`tab-item ${abaAtiva === 'CARTOES' ? 'active' : ''}`} onClick={() => setAbaAtiva('CARTOES')}>
                            Cartões e Informações
                        </span>
                        <span className={`tab-item ${abaAtiva === 'SUSPENSOES' ? 'active' : ''}`} onClick={() => setAbaAtiva('SUSPENSOES')}>
                            Suspensões
                        </span>
                        <span className={`tab-item ${abaAtiva === 'NOTAS' ? 'active' : ''}`} onClick={() => setAbaAtiva('NOTAS')}>
                            Atribuir Notas
                        </span>
                    </nav>
                </header>
                
                <div className="gestao-jogo-content">
                    {/* === ABA ESCALAÇÃO === */}
                    {/* Esta aba fica sempre visível para permitir CRIAR a escalação */}
                    {abaAtiva === 'ESCALACAO' && (
                        <>
                            <div className="campo-column">
                                <div className="campo-header-controls" style={{display:'flex', justifyContent:'space-between', marginBottom: '10px'}}>
                                    <span>Arraste os jogadores para o campo.</span>
                                    <button onClick={handleSalvarEscalacao} className="btn-salvar-escalacao">
                                        💾 Salvar Escalação
                                    </button>
                                </div>
                                <CampoTatico atletas={atletasNoCampo} fieldRef={campoTaticoRef} />
                            </div>
                            <div className="lista-column">
                                <div className="partida-selector-box" style={{marginBottom: '15px', padding: '10px', background: '#eef', borderRadius: '5px'}}>
                                    <label style={{display: 'block', fontWeight: 'bold', marginBottom: '5px'}}>Selecionar Partida:</label>
                                    <div style={{display: 'flex', gap: '5px'}}>
                                        <select value={partidaSelecionada} onChange={(e) => setPartidaSelecionada(e.target.value)} style={{flex: 1}}>
                                            <option value="">Selecione...</option>
                                            {partidas.map(p => (
                                                <option key={p.id} value={p.id}>
                                                    Jogo #{p.id} {p.dataJogo ? ` - ${new Date(p.dataJogo).toLocaleDateString()}` : ''}
                                                </option>
                                            ))}
                                        </select>
                                        <button className="btn-novo-jogo" onClick={() => setShowModalCriar(true)}>+</button>
                                    </div>
                                </div>
                                <div className="esquema-tatico-box">
                                    <label>Esquema Tático:</label>
                                    <input type="text" value={esquemaTatico} onChange={(e) => setEsquemaTatico(e.target.value)} className="input-esquema"/>
                                </div>
                                <ListaAtletas disponiveis={atletasDisponiveis} reservas={reservas} />
                            </div>
                        </>
                    )}

                    {/* === ABA CARTÕES (COM VERIFICAÇÃO) === */}
                    {abaAtiva === 'CARTOES' && (
                        <div style={{width: '100%', backgroundColor: 'white', padding: '20px', borderRadius: '8px', border: '1px solid #e0e0e0'}}>
                            {!escalacaoJaExiste ? (
                                <AvisoSemEscalacao />
                            ) : (
                                <AbaCartoesInfo 
                                    atletas={atletasRelacionados.length > 0 ? atletasRelacionados : todosAtletas}
                                    partidaId={partidaSelecionada}
                                />
                            )}
                        </div>
                    )}

                    {/* === ABA SUSPENSÕES === */}
                    {abaAtiva === 'SUSPENSOES' && (
                        <div style={{width: '100%', backgroundColor: 'white', padding: '20px', borderRadius: '8px', border: '1px solid #e0e0e0'}}>
                            <AbaSuspensoes clubeId={clubeIdLogado} />
                        </div>
                    )}

                    {/* === ABA NOTAS (COM VERIFICAÇÃO) === */}
                    {abaAtiva === 'NOTAS' && (
                        // Aqui fazemos a Verificação Condicional
                        !escalacaoJaExiste ? (
                            <AvisoSemEscalacao />
                        ) : (
                            <AbaAtribuirNotas 
                                atletas={atletasRelacionados.length > 0 ? atletasRelacionados : todosAtletas}
                                notasIniciais={notasDoBanco} 
                                onSalvar={handleSalvarNotas} 
                            />
                        )
                    )}
                </div>
            </div>

            {showModalCriar && (
                <CriarPartidaModal 
                    onClose={() => setShowModalCriar(false)} 
                    onSuccess={(novaPartida) => {
                        setPartidas([...partidas, novaPartida]);
                        setPartidaSelecionada(String(novaPartida.id));
                    }} 
                    clubeIdLogado={clubeIdLogado}
                />
            )}
        </DndContext>
    );
};