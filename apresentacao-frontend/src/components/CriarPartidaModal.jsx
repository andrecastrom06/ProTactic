import React, { useState, useEffect } from 'react';
import { buscarTodosClubes } from '../services/clubeService';
import { criarNovaPartida } from '../services/jogoService'; 
import '../components/NovoAtletaModal/NovoAtletaModal.css'; // Reutiliza estilos

export const CriarPartidaModal = ({ onClose, onSuccess, clubeIdLogado }) => {
    const [clubes, setClubes] = useState([]);
    const [adversarioId, setAdversarioId] = useState('');
    const [data, setData] = useState('');
    const [hora, setHora] = useState('');
    const [local, setLocal] = useState('Casa'); 

    useEffect(() => {
        const loadClubes = async () => {
            try {
                const lista = await buscarTodosClubes();
                // Remove o próprio clube da lista
                setClubes(lista.filter(c => c.id !== clubeIdLogado));
            } catch (e) { console.error(e); }
        };
        loadClubes();
    }, [clubeIdLogado]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!adversarioId || !data || !hora) {
            alert("Preencha todos os campos.");
            return;
        }

        const dados = {
            clubeCasaId: local === 'Casa' ? clubeIdLogado : parseInt(adversarioId),
            clubeVisitanteId: local === 'Casa' ? parseInt(adversarioId) : clubeIdLogado,
            dataJogo: data,
            hora: hora + ":00"
        };

        try {
            const novaPartida = await criarNovaPartida(dados);
            alert("Partida criada com sucesso!");
            onSuccess(novaPartida);
            onClose();
        } catch (err) {
            alert("Erro ao criar partida: " + err.message);
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