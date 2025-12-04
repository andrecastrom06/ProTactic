import React, { useState, useEffect } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { registrarInscricao } from '../../services/inscricaoService';
// Usando o CSS do modal de NovoAtleta para reaproveitar
import '../NovoAtletaModal/NovoAtletaModal.css'; 

// Lista de posições (como no protótipo)
const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];

export const RegistrarInscricaoModal = ({ competicaoNome, onClose, onSuccess, atletasJaInscritos }) => {
    const { clubeIdLogado } = useAuth();
    
    const [atletasDoClube, setAtletasDoClube] = useState([]);
    const [atletaSelecionadoId, setAtletaSelecionadoId] = useState('');
    
    const [posicao, setPosicao] = useState('');
    const [idade, setIdade] = useState('');
    const [carregandoAtletas, setCarregandoAtletas] = useState(true);
    const [carregando, setCarregando] = useState(false);
    useEffect(() => {
        const carregarAtletas = async () => {
            if (!clubeIdLogado) return;

            try {
                const todosAtletas = await buscarTodosJogadores();
                const atletasFiltrados = todosAtletas.filter(a => 
                    a.clubeId === clubeIdLogado && !atletasJaInscritos.has(a.nome)
                );
                setAtletasDoClube(atletasFiltrados);
            } catch (err) {
                console.error(err);
                Swal.fire({
                    title: 'Erro',
                    text: 'Não foi possível carregar a lista de atletas.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            } finally {
                setCarregandoAtletas(false);
            }
        };
        carregarAtletas();
    }, [clubeIdLogado, atletasJaInscritos]);

    const handleAtletaChange = (e) => {
        const id = e.target.value;
        setAtletaSelecionadoId(id);
        
        const atleta = atletasDoClube.find(a => a.id === parseInt(id, 10));
        if (atleta) {
            setPosicao(atleta.posicao || '');
            setIdade(atleta.idade || '');
        } else {
            setPosicao('');
            setIdade('');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const atleta = atletasDoClube.find(a => a.id === parseInt(atletaSelecionadoId, 10));
        
        if (!atleta || !posicao || !idade) {
            Swal.fire({
                title: 'Atenção',
                text: 'Por favor, preencha todos os campos obrigatórios.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setCarregando(true);

        try {
            const resultado = await registrarInscricao(atleta.nome, competicaoNome);
            
            if (resultado.inscrito) {
                await Swal.fire({
                    title: 'Sucesso!',
                    text: 'Atleta inscrito com sucesso!',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                });
                onSuccess(atleta.nome); 
            } else {
                Swal.fire({
                    title: 'Erro',
                    text: resultado.mensagemErro || "Falha ao registrar inscrição.",
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }

        } catch (error) {
            console.error(error);
            Swal.fire({
                title: 'Erro',
                text: error.message || "Ocorreu um erro inesperado.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        } finally {
            setCarregando(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Registrar Novo Atleta na Competição</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <p className="modal-subtitle">
                    Inscreva um novo atleta para participar da <strong>{competicaoNome}</strong>.
                </p>

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    <div className="form-group">
                        <label>Nome Completo *</label>
                        {carregandoAtletas ? (
                            <p>Carregando atletas...</p>
                        ) : (
                            <select value={atletaSelecionadoId} onChange={handleAtletaChange} disabled={carregando}>
                                <option value="">Selecione um atleta...</option>
                                {atletasDoClube.map(atleta => (
                                    <option key={atleta.id} value={atleta.id}>
                                        {atleta.nome}
                                    </option>
                                ))}
                            </select>
                        )}
                    </div>
                    
                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Posição *</label>
                            <select value={posicao} onChange={(e) => setPosicao(e.target.value)} disabled={carregando}>
                                <option value="">Selecione a posição</option>
                                {POSICOES.map(p => <option key={p} value={p}>{p}</option>)}
                            </select>
                        </div>
                        
                        <div className="form-group">
                            <label>Idade</label>
                            <input 
                                type="number" 
                                value={idade}
                                readOnly 
                                placeholder="-"
                                style={{ backgroundColor: '#f0f0f0' }} 
                            />
                        </div>
                    </div>
                    
                    <div className="modal-alert">
                        <strong>Importante:</strong> Apenas atletas com 16 anos ou mais podem ser inscritos na competição.
                    </div>


                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={carregando}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={carregando || carregandoAtletas}>
                            {carregando ? "Registrando..." : "Registrar Atleta"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};