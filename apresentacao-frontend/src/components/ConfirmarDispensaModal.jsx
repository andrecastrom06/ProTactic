import React, { useState } from 'react';
import { dispensarJogador } from '../services/contratoService';
import { useAuth } from '../store/AuthContext'; // 1. IMPORTAR O HOOK DE AUTH
import './NovoAtletaModal/NovoAtletaModal.css'; 

export const ConfirmarDispensaModal = ({ contrato, onClose, onSuccess }) => {
    const [loading, setLoading] = useState(false);
    
    // 2. RECUPERAR O USUÁRIO LOGADO
    const { usuario } = useAuth();

    const handleConfirm = async () => {
        // Validação de segurança no frontend
        if (!usuario || !usuario.id) {
            alert("Erro: Usuário não identificado na sessão.");
            return;
        }

        setLoading(true);
        try {
            // 3. PASSAR O ID DO USUÁRIO COMO SEGUNDO PARÂMETRO
            // O serviço contratoService.js já foi ajustado para enviar isso no Header 'usuarioId'
            await dispensarJogador(contrato.jogadorId, usuario.id);
            
            alert("Contrato encerrado e jogador dispensado.");
            onSuccess();
            onClose();
        } catch (err) {
            // Se o usuário não for ANALISTA, o erro 403 do backend aparecerá aqui
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