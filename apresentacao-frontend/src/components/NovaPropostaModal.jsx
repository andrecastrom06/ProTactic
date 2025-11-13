import React, { useState, useEffect, useMemo } from 'react';
import { buscarTodosClubes } from '../services/clubeService';
import { buscarTodosJogadores } from '../services/jogadorService';
import { criarProposta } from '../services/propostaService';

// Importa os estilos CSS que já criámos
import './NovaPropostaModal.css';

// Lista de posições (como no protótipo image_0ab587.png)
const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];
// Lista de durações (como no protótipo image_0ab5a5.png)
const DURACOES = ["1 ano", "2 anos", "3 anos", "4 anos", "< 5 anos"];

/**
 * O componente Modal com o formulário para registrar uma nova proposta.
 */
export const NovaPropostaModal = ({ onClose, onSuccess, clubePropositorId }) => {

    // --- Listas de Dados (vindas da API) ---
    const [todosJogadores, setTodosJogadores] = useState([]);
    const [todosClubes, setTodosClubes] = useState([]);
    const [carregandoDados, setCarregandoDados] = useState(true);
    const [erroApi, setErroApi] = useState(null);

    // --- Estado do Formulário ---
    const [jogadorIdSelecionado, setJogadorIdSelecionado] = useState('');
    const [clubeIdFiltro, setClubeIdFiltro] = useState(''); // Este é o "Clube Atual"
    const [idade, setIdade] = useState('');
    const [posicao, setPosicao] = useState('');
    const [salarioProposto, setSalarioProposto] = useState('');
    const [duracao, setDuracao] = useState('');
    const [observacoes, setObservacoes] = useState('');

    // --- Carrega os dados (Jogadores e Clubes) quando o modal abre ---
    useEffect(() => {
        const carregarListas = async () => {
            try {
                const [jogadores, clubes] = await Promise.all([
                    buscarTodosJogadores(),
                    buscarTodosClubes()
                ]);
                setTodosJogadores(jogadores);
                // Filtra o clube "Passes Livres" (ID 21) da lista de seleção
                setTodosClubes(clubes.filter(c => c.id !== 21));
            } catch (error) {
                setErroApi("Erro ao carregar listas: " + error.message);
            } finally {
                setCarregandoDados(false);
            }
        };
        carregarListas();
    }, []);

    // --- Lógica de Filtro Dinâmico (useMemo) ---
    // (Pedido 1: Selecionar clube filtra atletas)
    const jogadoresFiltrados = useMemo(() => {
        if (!clubeIdFiltro) {
            return todosJogadores; // Mostra todos
        }
        const idClube = parseInt(clubeIdFiltro, 10);
        
        // Com a correção no Backend, j.clubeId agora existe!
        return todosJogadores.filter(j => j.clubeId === idClube);

    }, [clubeIdFiltro, todosJogadores]);


    // --- Handlers (Funções de Ação) ---

    // Pedido 1: "se a pessoa comloca um clube especifico aparece so os atletas que existem la"
    const handleFiltroClubeChange = (e) => {
        const idClube = e.target.value;
        setClubeIdFiltro(idClube);
        
        // Limpa a seleção de jogador, pois a lista de opções mudou
        setJogadorIdSelecionado('');
        setIdade('');
        setPosicao('');
    };

    // Pedido 2: "quero que quando selecionar um atleta especifico ele ja apareca [os dados]"
    const handleJogadorChange = (e) => {
        const idJogador = e.target.value;
        setJogadorIdSelecionado(idJogador);

        if (!idJogador) {
            // Limpa tudo se o usuário selecionar "Selecione um atleta"
            setIdade('');
            setPosicao('');
            setClubeIdFiltro(''); // Limpa o filtro de clube
            return;
        }

        // Encontra o jogador completo na lista
        const jogador = todosJogadores.find(j => j.id === parseInt(idJogador, 10));
        
        if (jogador) {
            // Com a correção no Backend, j.idade e j.clubeId agora existem!
            setIdade(jogador.idade || 'N/A');
            setPosicao(jogador.posicao || ''); // Preenche a posição
            // Define o filtro de clube para o clube atual do jogador
            setClubeIdFiltro(jogador.clubeId || '');
        }
    };

    // Handler para o envio do formulário
    const handleSubmit = async (e) => {
        e.preventDefault(); 

        if (!jogadorIdSelecionado || !salarioProposto || !posicao || !duracao) {
            alert("Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        try {
            const formulario = {
                jogadorId: parseInt(jogadorIdSelecionado, 10),
                clubeId: clubePropositorId, // O ID do *nosso* clube
                valor: parseFloat(salarioProposto)
                // O backend não está a pedir Posição, Duração ou Observações ainda.
                // Estamos a enviá-los para o endpoint POST /criar
            };

            await criarProposta(formulario);
            
            alert("Proposta registrada com sucesso!");
            onSuccess(); 
            onClose(); 

        } catch (error) {
            alert(`Erro ao registrar proposta: ${error.message}`);
        }
    };


    // --- Renderização (HTML/JSX) ---
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Registrar Nova Proposta</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>

                {erroApi && <p style={{color: 'red'}}>{erroApi}</p>}

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    {carregandoDados ? <p>Carregando listas...</p> : (
                        <>
                            {/* Clube Atual (Filtro) */}
                            <div className="form-group">
                                <label>Clube Atual</label>
                                <select value={clubeIdFiltro} onChange={handleFiltroClubeChange}>
                                    <option value="">Todos os Clubes (Agentes Livres)</option>
                                    {todosClubes.map(clube => (
                                        <option key={clube.id} value={clube.id}>
                                            {clube.nome}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            {/* Nome do Atleta (Dropdown) */}
                            <div className="form-group">
                                <label>Nome do Atleta</label>
                                <select value={jogadorIdSelecionado} onChange={handleJogadorChange} required>
                                    <option value="">Selecione um atleta...</option>
                                    {jogadoresFiltrados.map(jogador => (
                                        <option key={jogador.id} value={jogador.id}>
                                            {jogador.nome}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            {/* Campos auto-preenchidos e Posição (Pedido 3) */}
                            <div className="form-group-row">
                                <div className="form-group">
                                    <label>Idade</label>
                                    <input type="text" value={idade} disabled />
                                </div>
                                
                                {/* --- (INÍCIO DA CORREÇÃO - PEDIDO 3) --- */}
                                <div className="form-group">
                                    <label>Posição</label>
                                    <select value={posicao} onChange={(e) => setPosicao(e.target.value)} required>
                                        <option value="">Selecione...</option>
                                        {POSICOES.map(p => (
                                            <option key={p} value={p}>{p}</option>
                                        ))}
                                    </select>
                                </div>
                                {/* --- (FIM DA CORREÇÃO - PEDIDO 3) --- */}
                            </div>
                            
                            {/* Salário e Duração (como em image_0ab5a5.png) */}
                            <div className="form-group-row">
                                <div className="form-group">
                                    <label>Salário Proposto</label>
                                    <input 
                                        type="number" 
                                        value={salarioProposto} 
                                        onChange={(e) => setSalarioProposto(e.target.value)}
                                        placeholder="Ex: 20000" 
                                        required 
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Duração (anos)</label>
                                    <select value={duracao} onChange={(e) => setDuracao(e.target.value)} required>
                                        <option value="">Selecione...</option>
                                        {DURACOES.map(d => (
                                            <option key={d} value={d}>{d}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </>
                    )}

                    {/* Botões de Ação */}
                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={carregandoDados}>
                            {carregandoDados ? "Carregando..." : "Registrar Proposta"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};