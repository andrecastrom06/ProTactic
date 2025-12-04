import React, { useState } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { salvarPremiacao } from '../services/premiacaoService';
import './NovoAtletaModal/NovoAtletaModal.css'; 

export const RegistrarPremiacaoModal = ({ onClose, onSuccess }) => {
    const [nomePremio, setNomePremio] = useState('');
    const [dataPremio, setDataPremio] = useState('');
    
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!nomePremio || !dataPremio) {
            Swal.fire({
                title: 'Atenção',
                text: 'Preencha o Nome do Prémio e a Data de Referência.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        setLoading(true);

        try {
            await salvarPremiacao(nomePremio, dataPremio); 
            
            await Swal.fire({
                title: 'Sucesso!',
                text: 'Premiação registrada! O sistema calculou o Jogador do Mês e o valor do prémio.',
                icon: 'success',
                timer: 2000,
                showConfirmButton: false
            });

            onSuccess();
            onClose();
            
            setNomePremio('');
            setDataPremio('');
            
        } catch (err) {
            console.error(err);
            Swal.fire({
                title: 'Erro ao salvar',
                text: err.message || "Não foi possível registrar a premiação.",
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
                    <h2>Gerar Premiação Automática</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    <div className="form-group">
                        <label>Nome do Prémio *</label>
                        <input 
                            type="text" 
                            value={nomePremio} 
                            onChange={e => setNomePremio(e.target.value)} 
                            placeholder="Ex: Melhor Jogador de Março"
                            disabled={loading}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Data de Referência (Mês/Ano) *</label>
                        <input 
                            type="date" 
                            value={dataPremio} 
                            onChange={e => setDataPremio(e.target.value)} 
                            disabled={loading}
                            required
                        />
                        <small style={{ marginTop: '8px', display: 'block', color: '#666' }}>
                            ℹ️ O sistema irá automaticamente:<br/>
                            1. Encontrar o jogador com a maior nota neste mês.<br/>
                            2. Calcular o valor financeiro (aplicando bónus de Capitão se aplicável).
                        </small>
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={loading}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Calculando...' : 'Gerar Premiação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};