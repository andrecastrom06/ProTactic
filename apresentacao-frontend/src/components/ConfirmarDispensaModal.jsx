import React, { useState } from 'react';
import { dispensarJogador } from '../services/contratoService';
import './NovoAtletaModal/NovoAtletaModal.css'; // Reutilizamos o CSS

export const ConfirmarDispensaModal = ({ contrato, onClose, onSuccess }) => {
    const [loading, setLoading] = useState(false);

    const handleConfirm = async () => {
        setLoading(true);
        try {
            // Usa o jogadorId que adicionamos no Backend anteriormente
            await dispensarJogador(contrato.jogadorId);
            alert("Contrato encerrado e jogador dispensado.");
            onSuccess();
            onClose();
        } catch (err) {
            alert("Erro ao encerrar: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2 style={{color: '#A00000'}}>Encerrar Contrato</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <div className="modal-alert" style={{backgroundColor: '#fff5f5', borderColor: '#fc8181', color: '#c53030'}}>
                    <strong>Atenção!</strong> Esta ação é irreversível.
                </div>

                <p style={{margin: '20px 0'}}>
                    Você tem certeza que deseja encerrar o contrato de <strong>{contrato.atletaNome}</strong>?
                    <br/>
                    O jogador será dispensado imediatamente do clube.
                </p>

                <div className="modal-actions">
                    <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                    <button 
                        type="button" 
                        className="btn-submit" 
                        onClick={handleConfirm}
                        disabled={loading}
                        style={{backgroundColor: '#A00000', borderColor: '#A00000'}}
                    >
                        {loading ? 'Encerrando...' : 'Sim, Encerrar Contrato'}
                    </button>
                </div>
            </div>
        </div>
    );
};