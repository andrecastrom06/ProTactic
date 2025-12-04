import React, { useState } from 'react';
import Swal from 'sweetalert2';
import { renovarContrato } from '../services/contratoService';
import './NovoAtletaModal/NovoAtletaModal.css';

export const RenovarContratoModal = ({ contrato, onClose, onSuccess }) => {
    const [duracao, setDuracao] = useState(12); // Padrão 12 meses
    const [salario, setSalario] = useState(contrato.salario || '');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!duracao || !salario) {
             Swal.fire({
                title: 'Atenção',
                text: 'Preencha a duração e o novo salário.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setLoading(true);

        const payload = {
            duracaoMeses: parseInt(duracao, 10),
            salario: parseFloat(salario),
            status: "ATIVO"
        };

        try {
            await renovarContrato(contrato.id, payload);
            
            await Swal.fire({
                title: 'Renovado!',
                text: 'Contrato atualizado com sucesso.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess();
            onClose();
        } catch (err) {
            console.error(err);
            Swal.fire({
                title: 'Erro na renovação',
                text: err.message || "Não foi possível renovar o contrato.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
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
                            disabled={loading}
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
                            disabled={loading}
                            required 
                        />
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={loading}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Salvando...' : 'Confirmar Renovação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};