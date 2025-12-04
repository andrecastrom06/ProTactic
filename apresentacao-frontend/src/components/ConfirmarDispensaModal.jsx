import React, { useState } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { dispensarJogador } from '../services/contratoService';
import { useAuth } from '../store/AuthContext';
import './NovoAtletaModal/NovoAtletaModal.css'; 

export const ConfirmarDispensaModal = ({ contrato, onClose, onSuccess }) => {
    const [loading, setLoading] = useState(false);
    
    const { usuario } = useAuth();

    const handleConfirm = async () => {
        if (!usuario || !usuario.id) {
            Swal.fire({
                title: 'Erro de Sessão',
                text: 'Usuário não identificado. Faça login novamente.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        setLoading(true);
        try {
            await dispensarJogador(contrato.jogadorId, usuario.id);
            
            await Swal.fire({
                title: 'Contrato Encerrado!',
                text: 'O jogador foi dispensado com sucesso.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess();
            onClose();

        } catch (err) {
            console.error("Erro ao dispensar:", err);
            
            Swal.fire({
                title: 'Falha ao encerrar',
                text: err.message || "Não foi possível dispensar o jogador.",
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
                    <button type="button" className="btn-cancel" onClick={onClose} disabled={loading}>
                        Cancelar
                    </button>
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