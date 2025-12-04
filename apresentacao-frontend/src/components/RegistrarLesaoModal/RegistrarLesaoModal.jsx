import React, { useState } from 'react';
import Swal from 'sweetalert2';
import { registrarNovaLesao } from '../../services/lesaoService';
import '../NovoAtletaModal/NovoAtletaModal.css'; 

const GRAUS_LESAO = [
    { valor: 1, nome: 'Grau 1 - Leve' },
    { valor: 2, nome: 'Grau 2 - Moderada' },
    { valor: 3, nome: 'Grau 3 - Grave' }
];

export const RegistrarLesaoModal = ({ atleta, onClose, onSuccess }) => {
    const [grau, setGrau] = useState('3'); 
    const [tipoLesao, setTipoLesao] = useState(''); 
    const [previsaoRetorno, setPrevisaoRetorno] = useState(''); 
    
    const [carregando, setCarregando] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!grau || !tipoLesao || !previsaoRetorno) {
            Swal.fire({
                title: 'Atenção',
                text: 'Todos os campos são obrigatórios.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }
        
        setCarregando(true);

        try {
            await registrarNovaLesao(
                atleta.id,
                grau,
                tipoLesao,
                previsaoRetorno
            );
            
            await Swal.fire({
                title: 'Sucesso!',
                text: 'Lesão registrada com sucesso!',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess();

        } catch (error) {
            console.error("Erro ao registrar lesão:", error);
            
            Swal.fire({
                title: 'Erro',
                text: error.message || "Falha ao registrar a lesão.",
                icon: 'error',
                confirmButtonText: 'OK'
            });
        } finally {
            setCarregando(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Registrar Lesão</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <p className="modal-subtitle">
                    Registrar nova lesão para <strong>{atleta.nome}</strong>.
                </p>

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    <div className="form-group">
                        <label>Grau da Lesão</label>
                        <select value={grau} onChange={(e) => setGrau(e.target.value)} disabled={carregando}>
                            {GRAUS_LESAO.map(g => (
                                <option key={g.valor} value={g.valor}>{g.nome}</option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Tipo de Lesão (Plano)</label>
                        <input 
                            type="text" 
                            value={tipoLesao} 
                            onChange={(e) => setTipoLesao(e.target.value)}
                            placeholder="Ex: Ligamento Cruzado (Joelho)"
                            disabled={carregando}
                        />
                    </div>
                    
                    <div className="form-group">
                        <label>Previsão de Retorno (Tempo)</label>
                        <input 
                            type="date" 
                            value={previsaoRetorno} 
                            onChange={(e) => setPrevisaoRetorno(e.target.value)}
                            disabled={carregando}
                        />
                    </div>

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={carregando}>Cancelar</button>
                        <button 
                            type="submit" 
                            className="btn-submit" 
                            disabled={carregando} 
                            style={{backgroundColor: '#dc3545'}}
                        >
                            {carregando ? "Registrando..." : "Registrar Lesão"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};