import React, { useState, useEffect } from 'react';
import { useAuth } from '../store/AuthContext';
import { buscarTodosJogadores } from '../services/jogadorService';
import { salvarPremiacao } from '../services/premiacaoService';
import './NovoAtletaModal/NovoAtletaModal.css'; // Reutiliza estilos existentes

export const RegistrarPremiacaoModal = ({ onClose, onSuccess }) => {
    const { clubeIdLogado } = useAuth();
    
    const [jogadores, setJogadores] = useState([]);
    const [jogadorId, setJogadorId] = useState('');
    const [nomePremio, setNomePremio] = useState('');
    const [dataPremio, setDataPremio] = useState('');
    
    const [loading, setLoading] = useState(false);

    // Carrega jogadores do clube para o dropdown
    useEffect(() => {
        const load = async () => {
            try {
                const lista = await buscarTodosJogadores();
                setJogadores(lista.filter(j => j.clubeId === clubeIdLogado));
            } catch (e) { console.error(e); }
        };
        load();
    }, [clubeIdLogado]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!jogadorId || !nomePremio || !dataPremio) {
            alert("Preencha todos os campos.");
            return;
        }

        setLoading(true);
        const payload = {
            jogadorId: parseInt(jogadorId, 10),
            nome: nomePremio,
            dataPremiacao: dataPremio // Formato yyyy-MM-dd do input html
        };

        try {
            await salvarPremiacao(payload);
            alert("Premiação registrada com sucesso!");
            onSuccess();
            onClose();
        } catch (err) {
            alert("Erro ao salvar: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Nova Premiação</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <form className="modal-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Jogador Vencedor *</label>
                        <select value={jogadorId} onChange={e => setJogadorId(e.target.value)} required>
                            <option value="">Selecione...</option>
                            {jogadores.map(j => (
                                <option key={j.id} value={j.id}>{j.nome}</option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Nome do Prémio *</label>
                        <input 
                            type="text" 
                            value={nomePremio} 
                            onChange={e => setNomePremio(e.target.value)} 
                            placeholder="Ex: Jogador do Mês"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Data da Premiação *</label>
                        <input 
                            type="date" 
                            value={dataPremio} 
                            onChange={e => setDataPremio(e.target.value)} 
                            required
                        />
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Salvando...' : 'Salvar Premiação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};