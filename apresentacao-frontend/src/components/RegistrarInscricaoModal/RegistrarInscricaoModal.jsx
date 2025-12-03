import React, { useState, useEffect } from 'react';
import { useAuth } from '../../store/AuthContext';
import { buscarTodosJogadores } from '../../services/jogadorService';
import { registrarInscricao } from '../../services/inscricaoService';
// Usando o CSS do modal de NovoAtleta para reaproveitar
import '../NovoAtletaModal/NovoAtletaModal.css'; 

// Lista de posições (como no protótipo)
const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];

export const RegistrarInscricaoModal = ({ competicaoNome, onClose, onSuccess, atletasJaInscritos }) => {
    const { clubeIdLogado } = useAuth();
    
    // Estados do formulário
    const [atletasDoClube, setAtletasDoClube] = useState([]);
    const [atletaSelecionadoId, setAtletaSelecionadoId] = useState('');
    
    // --- ALTERAÇÃO 1: Estado 'numeroCamisa' removido ---
    // const [numeroCamisa, setNumeroCamisa] = useState(''); // REMOVIDO
    
    const [posicao, setPosicao] = useState('');
    const [idade, setIdade] = useState('');
    const [carregandoAtletas, setCarregandoAtletas] = useState(true);
    
    const [erroApi, setErroApi] = useState(null);
    const [carregando, setCarregando] = useState(false);

    // Carrega os atletas do clube do usuário logado
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
                setErroApi(err.message);
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

    // Handler para o envio
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // --- ALTERAÇÃO 2: Validação atualizada (sem numeroCamisa) ---
        const atleta = atletasDoClube.find(a => a.id === parseInt(atletaSelecionadoId, 10));
        if (!atleta || !posicao || !idade) {
            setErroApi("Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        setCarregando(true);
        setErroApi(null);

        try {
            // O backend espera os Nomes, e não os IDs
            const resultado = await registrarInscricao(atleta.nome, competicaoNome);
            
            if (resultado.inscrito) {
                alert("Atleta inscrito com sucesso!");
                onSuccess(atleta.nome); // Passa o nome do atleta de volta
            } else {
                setErroApi(resultado.mensagemErro || "Falha ao registrar inscrição.");
            }

        } catch (error) {
            setErroApi(error.message);
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
                    
                    {/* Seletor de Atleta */}
                    <div className="form-group">
                        <label>Nome Completo *</label>
                        {carregandoAtletas ? (
                            <p>Carregando atletas...</p>
                        ) : (
                            <select value={atletaSelecionadoId} onChange={handleAtletaChange} required>
                                <option value="">Selecione um atleta...</option>
                                {atletasDoClube.map(atleta => (
                                    <option key={atleta.id} value={atleta.id}>
                                        {atleta.nome}
                                    </option>
                                ))}
                            </select>
                        )}
                    </div>
                    
                    {/* --- ALTERAÇÃO 3: Campo "Número da Camisa" REMOVIDO --- */}

                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Posição *</label>
                            <select value={posicao} onChange={(e) => setPosicao(e.target.value)} required>
                                <option value="">Selecione a posição</option>
                                {POSICOES.map(p => <option key={p} value={p}>{p}</option>)}
                            </select>
                        </div>
                        
                        {/* --- ALTERAÇÃO 4: Campo "Idade" agora é readOnly --- */}
                        <div className="form-group">
                            <label>Idade</label> {/* (O '*' foi removido) */}
                            <input 
                                type="number" 
                                value={idade}
                                readOnly // <-- Torna o campo não editável
                                placeholder="-"
                                style={{ backgroundColor: '#f0f0f0' }} // Estilo visual de "desabilitado"
                            />
                        </div>
                    </div>
                    
                    <div className="modal-alert">
                        <strong>Importante:</strong> Apenas atletas com 16 anos ou mais podem ser inscritos na competição.
                    </div>

                    {erroApi && <p className="error-message">{erroApi}</p>}

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={carregando || carregandoAtletas}>
                            {carregando ? "Registrando..." : "Registrar Atleta"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};