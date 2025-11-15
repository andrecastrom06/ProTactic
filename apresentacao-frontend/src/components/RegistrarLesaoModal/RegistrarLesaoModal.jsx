import React, { useState } from 'react';
import { registrarNovaLesao } from '../../services/lesaoService';
// Importa os estilos do outro modal para reaproveitar
import '../NovoAtletaModal/NovoAtletaModal.css'; 

// Opções de Grau de Lesão, CORRIGIDO para 1 a 3
const GRAUS_LESAO = [
    { valor: 1, nome: 'Grau 1 - Leve' },
    { valor: 2, nome: 'Grau 2 - Moderada' },
    { valor: 3, nome: 'Grau 3 - Grave' }
];

export const RegistrarLesaoModal = ({ atleta, onClose, onSuccess }) => {
    // Estados do formulário
    const [grau, setGrau] = useState('3'); // Default para Grau 3 como na imagem
    const [tipoLesao, setTipoLesao] = useState(''); // Ex: "Joelho"
    const [previsaoRetorno, setPrevisaoRetorno] = useState(''); // Data
    
    const [erroApi, setErroApi] = useState(null);
    const [carregando, setCarregando] = useState(false);

    // Handler para o envio
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!grau || !tipoLesao || !previsaoRetorno) {
            setErroApi("Todos os campos são obrigatórios.");
            return;
        }
        
        setCarregando(true);
        setErroApi(null);

        try {
            await registrarNovaLesao(
                atleta.id,
                grau,
                tipoLesao,
                previsaoRetorno // O backend salvará esta data como string no campo 'tempo'
            );
            
            alert("Lesão registrada com sucesso!");
            onSuccess(); // Fecha o modal e atualiza a lista de atletas

        } catch (error) {
            setErroApi(error.message);
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
                
                {/* Subtítulo dinâmico */}
                <p className="modal-subtitle">
                    Registrar nova lesão para {atleta.nome}.
                </p>

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    {/* Grau da Lesão (Dropdown) */}
                    <div className="form-group">
                        <label>Grau da Lesão</label>
                        <select value={grau} onChange={(e) => setGrau(e.target.value)}>
                            {/* O Grau 0 foi removido daqui */}
                            {GRAUS_LESAO.map(g => (
                                <option key={g.valor} value={g.valor}>{g.nome}</option>
                            ))}
                        </select>
                    </div>

                    {/* Tipo de Lesão (Input Texto) */}
                    <div className="form-group">
                        <label>Tipo de Lesão (Plano)</label>
                        <input 
                            type="text" 
                            value={tipoLesao} 
                            onChange={(e) => setTipoLesao(e.target.value)}
                            placeholder="Ex: Ligamento Cruzado (Joelho)"
                            required
                        />
                    </div>
                    
                    {/* Previsão de Retorno (Input Data) */}
                    <div className="form-group">
                        <label>Previsão de Retorno (Tempo)</label>
                        <input 
                            type="date" 
                            value={previsaoRetorno} 
                            onChange={(e) => setPrevisaoRetorno(e.target.value)}
                            required
                        />
                    </div>

                    {erroApi && <p className="error-message">{erroApi}</p>}

                    {/* Botões */}
                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={carregando} style={{backgroundColor: '#dc3545'}}>
                            {carregando ? "Registrando..." : "Registrar Lesão"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};