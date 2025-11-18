import React, { useState, useRef, useEffect, useCallback } from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';

import { CampoTatico } from './components/CampoTatico';
import { ListaAtletas } from './components/ListaAtletas';
import { AbaAtribuirNotas } from './components/AbaAtribuirNotas';
import { CriarPartidaModal } from '../../components/CriarPartidaModal'; 
import './GestaoJogoPage.css';

import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
// Importamos o serviço atualizado (ver passo 2 abaixo)
import { buscarJogosDoClube, salvarEscalacao, buscarEscalacaoPorPartida } from '../../services/jogoService'; 

export const GestaoJogoPage = () => {
    const { clubeIdLogado } = useAuth();
    const [abaAtiva, setAbaAtiva] = useState('ESCALACAO');

    // --- Estados de Dados ---
    const [todosAtletas, setTodosAtletas] = useState([]); // Cache de todos os atletas do clube
    const [atletasDisponiveis, setAtletasDisponiveis] = useState([]);
    const [reservas, setReservas] = useState([]); 
    const [atletasNoCampo, setAtletasNoCampo] = useState([]);
    
    const [partidas, setPartidas] = useState([]);
    const [partidaSelecionada, setPartidaSelecionada] = useState(''); // ID da partida (string para o select)
    
    const [esquemaTatico, setEsquemaTatico] = useState('4-3-3'); 
    const [showModalCriar, setShowModalCriar] = useState(false);

    const [loading, setLoading] = useState(true);
    const campoTaticoRef = useRef(null);

    // 1. Carrega Dados Iniciais (Jogadores e Partidas)
    useEffect(() => {
        const carregarDados = async () => {
            if (!clubeIdLogado) return;
            setLoading(true);
            try {
                // A. Busca Jogadores
                const players = await buscarTodosJogadores();
                // Filtra: Do meu clube + Saudáveis + Contrato Ativo
                const meusJogadores = players.filter(j => 
                    j.clubeId === clubeIdLogado && j.saudavel && j.contratoAtivo
                ).map(j => ({ ...j, numero: j.id, posicao: j.posicao || '?' }));
                
                setTodosAtletas(meusJogadores);
                setAtletasDisponiveis(meusJogadores); // Inicialmente todos disponíveis

                // B. Busca Partidas
                const jogos = await buscarJogosDoClube(clubeIdLogado);
                setPartidas(jogos);
                
                // Seleciona a primeira partida automaticamente se existir
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

    // 2. Carrega a Escalação quando muda a Partida Selecionada
    useEffect(() => {
        const carregarEscalacao = async () => {
            if (!partidaSelecionada || todosAtletas.length === 0) return;

            try {
                // Reseta as listas para o estado inicial (todos disponíveis)
                setAtletasNoCampo([]);
                setReservas([]);
                // Cria uma cópia para manipular
                let poolDeAtletas = [...todosAtletas];

                // Busca no backend
                const escalacao = await buscarEscalacaoPorPartida(partidaSelecionada);
                
                if (escalacao) {
                    // Se já existe escalação salva, aplica:
                    setEsquemaTatico(escalacao.esquema || '4-3-3');
                    
                    // Recupera IDs dos 11 titulares
                    const idsTitulares = [
                        escalacao.idJogador1, escalacao.idJogador2, escalacao.idJogador3,
                        escalacao.idJogador4, escalacao.idJogador5, escalacao.idJogador6,
                        escalacao.idJogador7, escalacao.idJogador8, escalacao.idJogador9,
                        escalacao.idJogador10, escalacao.idJogador11
                    ].filter(id => id !== null); 

                    // Distribui os jogadores
                    const novosTitulares = [];
                    
                    idsTitulares.forEach(id => {
                        const index = poolDeAtletas.findIndex(a => a.id === id);
                        if (index !== -1) {
                            // Move do pool para titulares
                            const atleta = poolDeAtletas[index];
                            // (Futuramente podemos salvar coordenadas X/Y no banco, por enquanto fixamos centro)
                            novosTitulares.push({ ...atleta, position: { x: 50, y: 50 } });
                            poolDeAtletas.splice(index, 1); // Remove do pool
                        }
                    });

                    setAtletasNoCampo(novosTitulares);
                    setAtletasDisponiveis(poolDeAtletas); // O resto fica disponível
                } else {
                    // Se não tem escalação salva, reseta tudo
                    setAtletasDisponiveis(todosAtletas);
                }
            } catch (error) {
                console.error("Erro ao carregar escalação:", error);
            }
        };
        carregarEscalacao();
    }, [partidaSelecionada, todosAtletas]);


    // 3. Lógica de Drag & Drop
    const handleDragEnd = (event) => {
        const { active, over, delta } = event;
        if (!over) return; 

        const atletaArrastado = active.data.current.atleta;
        const tipoOrigem = active.data.current.type; 
        const idDestino = over.id; 
        const campoRect = campoTaticoRef.current?.getBoundingClientRect();

        // Movimento dentro do campo
        if (tipoOrigem === 'NO_CAMPO' && idDestino === 'CAMPO_TATICO') {
            if (!campoRect) return;
            const percentDeltaX = (delta.x / campoRect.width) * 100;
            const percentDeltaY = (delta.y / campoRect.height) * 100;
            setAtletasNoCampo(items => items.map(item =>
                item.id === active.id ? { ...item, position: { x: item.position.x + percentDeltaX, y: item.position.y + percentDeltaY } } : item
            ));
            return;
        }
        
        // Remove da origem
        if (tipoOrigem === 'DISPONIVEL') setAtletasDisponiveis(p => p.filter(a => a.id !== atletaArrastado.id));
        else if (tipoOrigem === 'RESERVA') setReservas(p => p.filter(a => a.id !== atletaArrastado.id));
        else if (tipoOrigem === 'NO_CAMPO') setAtletasNoCampo(p => p.filter(a => a.id !== atletaArrastado.id));

        const { position, ...atletaLimpo } = atletaArrastado;

        // Adiciona no destino
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
            if (atletasNoCampo.length >= 11) {
                alert("Atenção: Já existem 11 jogadores em campo.");
            }
            setAtletasNoCampo(prev => [...prev, { ...atletaLimpo, position: newPosition }]);
        } else if (idDestino === 'DISPONIVEIS_LIST') {
            setAtletasDisponiveis(p => [...p, atletaLimpo]);
        } else if (idDestino === 'RESERVAS_LIST') {
            setReservas(p => [...p, atletaLimpo]);
        }
    };

    // 4. Salvar no Backend
    const handleSalvarEscalacao = async () => {
        if (!partidaSelecionada) {
            alert("Selecione uma partida primeiro.");
            return;
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
        } catch (err) {
            alert("Erro ao salvar: " + err.message);
        }
    };

    // Callback do Modal de Criar Partida
    const handlePartidaCriada = (novaPartida) => {
        setPartidas([...partidas, novaPartida]);
        setPartidaSelecionada(String(novaPartida.id)); // Seleciona a nova
    };

    // Filtra quem aparece na aba de notas: Só quem está no campo ou reserva
    const atletasParaAvaliacao = [...atletasNoCampo, ...reservas];
    
    const partidaObj = partidas.find(p => p.id === Number(partidaSelecionada));
    const tituloPartida = partidaObj ? `Jogo: ${partidaObj.id}` : "Selecione uma Partida";

    if (loading) return <div style={{padding:20}}>Carregando dados...</div>;

    return (
        <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <div className="gestao-jogo-page">
                
                <header className="gestao-jogo-header">
                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <h2>{tituloPartida}</h2>
                        {/* O seletor foi removido daqui para limpar o header, conforme pediste */}
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
                                
                                {/* --- NOVO: Seletor de Partida na coluna direita --- */}
                                <div className="partida-selector-box" style={{marginBottom: '15px', padding: '10px', background: '#eef', borderRadius: '5px'}}>
                                    <label style={{display: 'block', fontWeight: 'bold', marginBottom: '5px'}}>Selecionar Partida:</label>
                                    <div style={{display: 'flex', gap: '5px'}}>
                                        <select 
                                            value={partidaSelecionada} 
                                            onChange={(e) => setPartidaSelecionada(e.target.value)}
                                            style={{flex: 1, padding: '8px', borderRadius: '4px', border: '1px solid #ccc'}}
                                        >
                                            <option value="">Selecione...</option>
                                            {partidas.map(p => (
                                                <option key={p.id} value={p.id}>
                                                    {/* Formata a data se existir, ou usa ID */}
                                                    Jogo #{p.id} {p.dataJogo ? ` - ${new Date(p.dataJogo).toLocaleDateString()}` : ''}
                                                </option>
                                            ))}
                                        </select>
                                        <button className="btn-novo-jogo" onClick={() => setShowModalCriar(true)} style={{fontSize: '18px', padding: '0 10px'}}>
                                            +
                                        </button>
                                    </div>
                                </div>

                                <div className="esquema-tatico-box">
                                    <label>Esquema Tático:</label>
                                    <input 
                                        type="text" 
                                        value={esquemaTatico} 
                                        onChange={(e) => setEsquemaTatico(e.target.value)}
                                        placeholder="ex: 4-3-3"
                                        className="input-esquema"
                                    />
                                </div>

                                <ListaAtletas disponiveis={atletasDisponiveis} reservas={reservas} />
                            </div>
                        </>
                    )}

                    {abaAtiva === 'NOTAS' && (
                        <AbaAtribuirNotas 
                            atletas={atletasParaAvaliacao} // Passa apenas quem jogou
                            onSalvar={(notas) => console.log("Salvar notas (TODO):", notas)} 
                        />
                    )}
                </div>
            </div>

            {showModalCriar && (
                <CriarPartidaModal 
                    onClose={() => setShowModalCriar(false)} 
                    onSuccess={handlePartidaCriada} 
                    clubeIdLogado={clubeIdLogado}
                />
            )}
        </DndContext>
    );
};