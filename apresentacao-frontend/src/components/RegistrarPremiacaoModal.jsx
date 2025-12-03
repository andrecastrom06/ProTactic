import React, { useState } from 'react';
import { salvarPremiacao } from '../services/premiacaoService';
import './NovoAtletaModal/NovoAtletaModal.css'; 

export const RegistrarPremiacaoModal = ({ onClose, onSuccess }) => {
    const [nomePremio, setNomePremio] = useState('');
    const [dataPremio, setDataPremio] = useState('');
    
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!nomePremio || !dataPremio) {
            alert("Preencha o Nome do Prémio e a Data de Referência.");
            return;
        }

        setLoading(true);

        try {
            // Não enviamos jogadorId, o backend calcula isso
            await salvarPremiacao(nomePremio, dataPremio); 
            
            alert("Premiação registrada com sucesso! O sistema calculou o Jogador do Mês e o valor do prémio.");
            onSuccess();
            onClose();
            
            setNomePremio('');
            setDataPremio('');
            
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
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Data de Referência (Mês/Ano) *</label>
                        <input 
                            type="date" 
                            value={dataPremio} 
                            onChange={e => setDataPremio(e.target.value)} 
                            required
                        />
                        <small style={{ marginTop: '8px', display: 'block', color: '#666' }}>
                            ℹ️ O sistema irá automaticamente:<br/>
                            1. Encontrar o jogador com a maior nota neste mês.<br/>
                            2. Calcular o valor financeiro (aplicando bónus de Capitão se aplicável).
                        </small>
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Calculando e Salvando...' : 'Gerar Premiação'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};