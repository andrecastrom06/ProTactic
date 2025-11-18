import React, { useState, useRef, useEffect } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';
import { AbaAtribuirNotas } from './components/AbaAtribuirNotas';
import { CriarPartidaModal } from '../../components/CriarPartidaModal'; 
import './GestaoJogoPage.css';

import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { buscarJogosDoClube, salvarEscalacao, buscarEscalacaoPorPartida } from '../../services/jogoService'; 
// IMPORTA OS SERVIÇOS DE NOTAS
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
    
    // Estado para controle de sobrescrita
    const [escalacaoJaExiste, setEscalacaoJaExiste] = useState(false);

    // Estado para guardar as notas vindas do banco
    const [notasDoBanco, setNotasDoBanco] = useState([]);

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

    // 2. Carrega a Escalação E AS NOTAS quando muda a Partida Selecionada
    useEffect(() => {
        const carregarDetalhesPartida = async () => {
            if (!partidaSelecionada || todosAtletas.length === 0) return;

            try {
                // Reset visual
                setAtletasNoCampo([]);
                setReservas([]);
                setEscalacaoJaExiste(false);
                setNotasDoBanco([]); // Limpa notas antigas

                let poolDeAtletas = [...todosAtletas];

                // --- A. Busca Escalação ---
                const escalacao = await buscarEscalacaoPorPartida(partidaSelecionada);
                
                if (escalacao) {
                    setEscalacaoJaExiste(true); // Ativa flag de aviso
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

                // --- B. Busca Notas ---
                // O backend espera "JOGO-ID" como string para a chave composta
                const notas = await buscarNotasPorJogo("JOGO-" + partidaSelecionada);
                setNotasDoBanco(notas);

            } catch (error) {
                console.error("Erro ao carregar detalhes:", error);
            }
        };
        carregarDetalhesPartida();
    }, [partidaSelecionada, todosAtletas]);

    // 3. Drag & Drop (Inalterado)
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

    // 4. Salvar Escalação COM AVISO
    const handleSalvarEscalacao = async () => {
        if (!partidaSelecionada) {
            alert("Selecione uma partida primeiro.");
            return;
        }
        
        // Lógica do Aviso
        if (escalacaoJaExiste) {
            const confirmar = window.confirm("Já existe uma escalação salva para este jogo. Ao salvar a nova escalação, a antiga será substituída. Deseja continuar?");
            if (!confirmar) return;
        }

        if (atletasNoCampo.length !== 11) {
            if(!window.confirm(`Tens ${atletasNoCampo.length} jogadores em campo. O jogo requer 11. Salvar mesmo assim?`)) {
                return;
            }
        }

        const idsJogadores = atletasNoCampo.map(a => a.id);
        const payload = {
            partidaId: parseInt(partidaSelecionada, 10),
            esquema: esquemaTatico,
            jogadoresIds: idsJogadores
        };

        try {
            await salvarEscalacao(payload);
            alert("Escalação salva com sucesso!");
            setEscalacaoJaExiste(true); // Atualiza para evitar duplo aviso imediato
        } catch (err) {
            alert("Erro ao salvar: " + err.message);
        }
    };

    // 5. Salvar Notas (Integrado)
    const handleSalvarNotas = async (avaliacoes) => {
        if (!partidaSelecionada) return;
        try {
            const promises = Object.entries(avaliacoes).map(([jogadorIdStr, dados]) => {
                // Envia se tiver nota OU observação
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
            alert("Avaliações salvas com sucesso!");
        } catch (error) {
            alert("Erro ao salvar avaliações: " + error.message);
        }
    };

    // Jogadores elegíveis para nota (Titulares + Reservas definidos na aba escalação)
    // Nota: Se quiseres que apareçam TODOS, usa 'todosAtletas'. 
    // Aqui usamos 'atletasNoCampo + reservas' assumindo que o usuário montou o time.
    const atletasParaAvaliacao = [...atletasNoCampo, ...reservas]; 
    // Se a lista estiver vazia (ex: user foi direto para notas), usa todosAtletas como fallback seguro?
    const listaFinalAvaliacao = atletasParaAvaliacao.length > 0 ? atletasParaAvaliacao : todosAtletas;

    const partidaObj = partidas.find(p => p.id === Number(partidaSelecionada));
    const tituloPartida = partidaObj ? `Jogo: ${partidaObj.id}` : "Selecione uma Partida";

    if (loading) return <div style={{padding:20}}>Carregando dados...</div>;

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                <header className="gestao-jogo-header">
                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <h2>{tituloPartida}</h2>
                    </div>
                    <nav className="gestao-jogo-tabs">
                        <span className={`tab-item ${abaAtiva === 'ESCALACAO' ? 'active' : ''}`} onClick={() => setAbaAtiva('ESCALACAO')}>
                            Escalação Tática
                        </span>
                        <span className={`tab-item ${abaAtiva === 'NOTAS' ? 'active' : ''}`} onClick={() => setAbaAtiva('NOTAS')}>
                            Atribuir Notas
                        </span>
                    </nav>
                </header>
                
                <div className="gestao-jogo-content">
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

                    {abaAtiva === 'NOTAS' && (
                        <AbaAtribuirNotas 
                            atletas={listaFinalAvaliacao}
                            notasIniciais={notasDoBanco} // Passa as notas carregadas
                            onSalvar={handleSalvarNotas} 
                        />
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