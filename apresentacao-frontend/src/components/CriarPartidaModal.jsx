import React, { useState, useEffect } from 'react';
import { buscarTodosClubes } from '../services/clubeService';
import { criarNovaPartida } from '../services/jogoService'; 
import '../components/NovoAtletaModal/NovoAtletaModal.css';

export const CriarPartidaModal = ({ onClose, onSuccess, clubeIdLogado }) => {
    const [clubes, setClubes] = useState([]);
    const [adversarioId, setAdversarioId] = useState('');
    const [data, setData] = useState('');
    const [hora, setHora] = useState('');
    const [local, setLocal] = useState('Casa'); 
    const [error, setError] = useState(''); // Adicionado estado para mostrar erro no modal

    useEffect(() => {
        const loadClubes = async () => {
            try {
                const lista = await buscarTodosClubes();
                setClubes(lista.filter(c => c.id !== clubeIdLogado));
            } catch (e) { 
                console.error("Erro ao carregar clubes:", e);
                setError("Não foi possível carregar a lista de clubes.");
            }
        };
        loadClubes();
    }, [clubeIdLogado]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!adversarioId || !data || !hora) {
            setError("Preencha todos os campos.");
            return;
        }

        // CORREÇÃO: Removemos a concatenação desnecessária de ":00"
        // ... dentro de handleSubmit
        const dados = {
            clubeCasaId: local === 'Casa' ? clubeIdLogado : parseInt(adversarioId),
            clubeVisitanteId: local === 'Casa' ? parseInt(adversarioId) : clubeIdLogado,
            dataJogo: data, // Deve ser STRING pura "YYYY-MM-DD"
            hora: hora // Deve ser STRING pura "HH:MM"
        };


        try {
            const novaPartida = await criarNovaPartida(dados);
            alert("Partida criada com sucesso!");
            onSuccess(novaPartida);
            onClose();
        } catch (err) {
            console.error("Detalhes do erro:", err);
            // Mensagem mais informativa
            setError("Erro ao criar partida. (400 ou 405). Por favor, reinicie o servidor Java e verifique a formatação.");
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
                    {error && <p className="error-message" style={{color: 'red', marginBottom: '10px'}}>{error}</p>}
                    
                    <div className="form-group">
                        <label>Mando de Campo</label>
                        <select value={local} onChange={e => setLocal(e.target.value)}>
                            <option value="Casa">Em Casa (Mandante)</option>
                            <option value="Fora">Fora (Visitante)</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Adversário</label>
                        <select value={adversarioId} onChange={e => setAdversarioId(e.target.value)} required>
                            <option value="">Selecione...</option>
                            {clubes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                        </select>
                    </div>
                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Data</label>
                            <input type="date" value={data} onChange={e => setData(e.target.value)} required />
                        </div>
                        <div className="form-group">
                            <label>Hora</label>
                            <input type="time" value={hora} onChange={e => setHora(e.target.value)} required />
                        </div>
                    </div>
                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit">Criar Jogo</button>
                    </div>
                </form>
            </div>
        </div>
    );
};