import React, { useState, useEffect } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { buscarTodosClubes } from '../services/clubeService';
import { criarNovaPartida } from '../services/jogoService'; 
import '../components/NovoAtletaModal/NovoAtletaModal.css';

export const CriarPartidaModal = ({ onClose, onSuccess, clubeIdLogado }) => {
    const [clubes, setClubes] = useState([]);
    const [adversarioId, setAdversarioId] = useState('');
    const [data, setData] = useState('');
    const [hora, setHora] = useState('');
    const [local, setLocal] = useState('Casa'); 
    
    // Estado de loading para bloquear o botão
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        const loadClubes = async () => {
            try {
                const lista = await buscarTodosClubes();
                setClubes(lista.filter(c => c.id !== clubeIdLogado));
            } catch (e) { 
                console.error("Erro ao carregar clubes:", e);
                // Erro ao carregar a lista (Opcional: mostrar aviso)
                Swal.fire({
                    title: 'Erro',
                    text: 'Não foi possível carregar a lista de clubes adversários.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        };
        loadClubes();
    }, [clubeIdLogado]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 1. Validação (Warning)
        if (!adversarioId || !data || !hora) {
            Swal.fire({
                title: 'Atenção',
                text: 'Preencha todos os campos obrigatórios.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setIsSubmitting(true);

        // Adicionamos ":00" se a hora vier apenas como HH:mm
        const horaFormatada = hora.length === 5 ? hora + ":00" : hora;

        const dados = {
            clubeCasaId: local === 'Casa' ? clubeIdLogado : parseInt(adversarioId),
            clubeVisitanteId: local === 'Casa' ? parseInt(adversarioId) : clubeIdLogado,
            dataJogo: data, 
            hora: horaFormatada // Envia HH:mm:ss
        };

        try {
            const novaPartida = await criarNovaPartida(dados);
            
            // 2. Sucesso (Success)
            await Swal.fire({
                title: 'Sucesso!',
                text: 'Partida criada com sucesso!',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess(novaPartida);
            onClose();
        } catch (err) {
            console.error("Detalhes do erro:", err);
            
            // 3. Erro (Error)
            Swal.fire({
                title: 'Erro',
                text: "Erro ao criar partida. Verifique a conexão com o servidor.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Criar Nova Partida</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <form className="modal-form" onSubmit={handleSubmit}>
                    {/* Mensagem de erro visual removida, substituída pelo Swal */}
                    
                    <div className="form-group">
                        <label>Mando de Campo</label>
                        <select value={local} onChange={e => setLocal(e.target.value)} disabled={isSubmitting}>
                            <option value="Casa">Em Casa (Mandante)</option>
                            <option value="Fora">Fora (Visitante)</option>
                        </select>
                    </div>
                    
                    <div className="form-group">
                        <label>Adversário</label>
                        <select value={adversarioId} onChange={e => setAdversarioId(e.target.value)} required disabled={isSubmitting}>
                            <option value="">Selecione...</option>
                            {clubes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                        </select>
                    </div>
                    
                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Data</label>
                            <input 
                                type="date" 
                                value={data} 
                                onChange={e => setData(e.target.value)} 
                                required 
                                disabled={isSubmitting}
                            />
                        </div>
                        <div className="form-group">
                            <label>Hora</label>
                            <input 
                                type="time" 
                                value={hora} 
                                onChange={e => setHora(e.target.value)} 
                                required 
                                disabled={isSubmitting}
                            />
                        </div>
                    </div>
                    
                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={isSubmitting}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Criando...' : 'Criar Jogo'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};