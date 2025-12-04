import React, { useState, useEffect, useMemo } from 'react';
import Swal from 'sweetalert2'; 
import { buscarTodosClubes } from '../services/clubeService';
import { buscarTodosJogadores } from '../services/jogadorService';
import { criarProposta } from '../services/propostaService';

import './NovaPropostaModal.css';

const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];
const DURACOES = ["1 ano", "2 anos", "3 anos", "4 anos", "< 5 anos"];

export const NovaPropostaModal = ({ onClose, onSuccess, clubePropositorId }) => {

    const [todosJogadores, setTodosJogadores] = useState([]);
    const [todosClubes, setTodosClubes] = useState([]);
    
    const [carregandoDados, setCarregandoDados] = useState(true);
    
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [jogadorIdSelecionado, setJogadorIdSelecionado] = useState('');
    const [clubeIdFiltro, setClubeIdFiltro] = useState(''); // Este é o "Clube Atual"
    const [idade, setIdade] = useState('');
    const [posicao, setPosicao] = useState('');
    const [salarioProposto, setSalarioProposto] = useState('');
    const [duracao, setDuracao] = useState('');

    useEffect(() => {
        const carregarListas = async () => {
            try {
                const [jogadores, clubes] = await Promise.all([
                    buscarTodosJogadores(),
                    buscarTodosClubes()
                ]);
                setTodosJogadores(jogadores);
                setTodosClubes(clubes.filter(c => c.id !== 21));
            } catch (error) {
                console.error(error);
                Swal.fire({
                    title: 'Erro',
                    text: 'Não foi possível carregar a lista de atletas ou clubes.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            } finally {
                setCarregandoDados(false);
            }
        };
        carregarListas();
    }, []);

    const jogadoresFiltrados = useMemo(() => {
        if (!clubeIdFiltro) {
            return todosJogadores; // Mostra todos
        }
        const idClube = parseInt(clubeIdFiltro, 10);
        return todosJogadores.filter(j => j.clubeId === idClube);

    }, [clubeIdFiltro, todosJogadores]);



    const handleFiltroClubeChange = (e) => {
        const idClube = e.target.value;
        setClubeIdFiltro(idClube);
        setJogadorIdSelecionado('');
        setIdade('');
        setPosicao('');
    };

    const handleJogadorChange = (e) => {
        const idJogador = e.target.value;
        setJogadorIdSelecionado(idJogador);

        if (!idJogador) {
            setIdade('');
            setPosicao('');
            setClubeIdFiltro('');
            return;
        }

        const jogador = todosJogadores.find(j => j.id === parseInt(idJogador, 10));
        
        if (jogador) {
            setIdade(jogador.idade || 'N/A');
            setPosicao(jogador.posicao || ''); 
            setClubeIdFiltro(jogador.clubeId || '');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); 
        if (!jogadorIdSelecionado || !salarioProposto || !posicao || !duracao) {
            Swal.fire({
                title: 'Atenção',
                text: 'Por favor, preencha todos os campos obrigatórios.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setIsSubmitting(true);

        try {
            const formulario = {
                jogadorId: parseInt(jogadorIdSelecionado, 10),
                clubeId: clubePropositorId, 
                valor: parseFloat(salarioProposto)
            };

            await criarProposta(formulario);
            await Swal.fire({
                title: 'Sucesso!',
                text: 'Proposta registrada e enviada.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess(); 
            onClose(); 

        } catch (error) {
            console.error(error);
            Swal.fire({
                title: 'Erro',
                text: `Falha ao registrar proposta: ${error.message || 'Erro desconhecido'}`,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Registrar Nova Proposta</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    {carregandoDados ? <p>Carregando listas...</p> : (
                        <>
                            <div className="form-group">
                                <label>Clube Atual</label>
                                <select value={clubeIdFiltro} onChange={handleFiltroClubeChange} disabled={isSubmitting}>
                                    <option value="">Todos os Clubes (Agentes Livres)</option>
                                    {todosClubes.map(clube => (
                                        <option key={clube.id} value={clube.id}>
                                            {clube.nome}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Nome do Atleta</label>
                                <select value={jogadorIdSelecionado} onChange={handleJogadorChange} required disabled={isSubmitting}>
                                    <option value="">Selecione um atleta...</option>
                                    {jogadoresFiltrados.map(jogador => (
                                        <option key={jogador.id} value={jogador.id}>
                                            {jogador.nome}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group-row">
                                <div className="form-group">
                                    <label>Idade</label>
                                    <input type="text" value={idade} disabled />
                                </div>
                                
                                <div className="form-group">
                                    <label>Posição</label>
                                    <select value={posicao} onChange={(e) => setPosicao(e.target.value)} required disabled={isSubmitting}>
                                        <option value="">Selecione...</option>
                                        {POSICOES.map(p => (
                                            <option key={p} value={p}>{p}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                            
                            <div className="form-group-row">
                                <div className="form-group">
                                    <label>Salário Proposto</label>
                                    <input 
                                        type="number" 
                                        value={salarioProposto} 
                                        onChange={(e) => setSalarioProposto(e.target.value)}
                                        placeholder="Ex: 20000" 
                                        required 
                                        disabled={isSubmitting}
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Duração (anos)</label>
                                    <select value={duracao} onChange={(e) => setDuracao(e.target.value)} required disabled={isSubmitting}>
                                        <option value="">Selecione...</option>
                                        {DURACOES.map(d => (
                                            <option key={d} value={d}>{d}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </>
                    )}

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={isSubmitting}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={carregandoDados || isSubmitting}>
                            {isSubmitting ? "Enviando..." : "Registrar Proposta"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};