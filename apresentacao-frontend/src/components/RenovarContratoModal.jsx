import React, { useState } from 'react';
import { renovarContrato } from '../services/contratoService';
import './NovoAtletaModal/NovoAtletaModal.css'; // Reutilizamos o CSS padrão do projeto

export const RenovarContratoModal = ({ contrato, onClose, onSuccess }) => {
    // Estados iniciais
    const [duracao, setDuracao] = useState(12); // Padrão 12 meses
    const [salario, setSalario] = useState(contrato.salario || '');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        const payload = {
            duracaoMeses: parseInt(duracao, 10),
            salario: parseFloat(salario),
            status: "ATIVO"
        };

        try {
            await renovarContrato(contrato.id, payload);
            alert("Contrato renovado com sucesso!");
            onSuccess();
            onClose();
        } catch (err) {
            alert("Erro ao renovar: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Renovar Contrato</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <p className="modal-subtitle">Defina os novos termos para <strong>{contrato.atletaNome}</strong>.</p>

                <form className="modal-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Nova Duração (meses)</label>
                        <input 
                            type="number" 
                            value={duracao} 
                            onChange={e => setDuracao(e.target.value)} 
                            min="1"
                            required 
                        />
                    </div>
                    <div className="form-group">
                        <label>Novo Salário</label>
                        <input 
                            type="number" 
                            value={salario} 
                            onChange={e => setSalario(e.target.value)} 
                            step="0.01"
                            required 
                        />
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Salvando...' : 'Confirmar Renovação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};