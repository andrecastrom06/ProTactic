// src/pages/GestaoJogo/components/AbaCartoesInfo.jsx
import React, { useState } from 'react';
import './AbaAtribuirNotas.css'; // Reutilizamos o estilo da tabela

export const AbaCartoesInfo = ({ atletas, partidaId }) => {
    // Estado local para armazenar as estatísticas desta sessão
    const [stats, setStats] = useState({});
    
    // Helper para atualizar estado
    const updateStat = (id, field, value) => {
        setStats(prev => ({
            ...prev,
            [id]: {
                ...prev[id],
                [field]: value
            }
        }));
    };

    const handleAddCartao = (atletaId, tipo) => {
        const currentQtd = stats[atletaId]?.[tipo] || 0;
        updateStat(atletaId, tipo, currentQtd + 1);
        
        // Simulação de envio para o backend
        console.log(`Registrando cartão ${tipo} para atleta ${atletaId} na partida ${partidaId}`);
        
        // AQUI SERIA A CHAMADA API REAL:
        // await registrarCartao({ atletaId, partidaId, tipo });
        alert(`Cartão ${tipo === 'amarelo' ? 'Amarelo' : 'Vermelho'} registrado!`);
    };

    return (
        <div className="aba-notas-container">
            <div className="aba-notas-header-actions">
                <h3 style={{margin: 0, color: '#333'}}>Registro de Eventos da Partida</h3>
                <button className="btn-salvar-notas" onClick={() => alert("Estatísticas salvas!")}>
                    Salvar Estatísticas
                </button>
            </div>

            <div className="aba-notas-tabela-wrapper">
                <table className="aba-notas-tabela">
                    <thead>
                        <tr>
                            <th style={{width: '30%'}}>Atleta</th>
                            <th style={{width: '15%', textAlign: 'center'}}>Gols</th>
                            <th style={{width: '15%', textAlign: 'center'}}>Assistências</th>
                            <th style={{width: '40%', textAlign: 'center'}}>Cartões</th>
                        </tr>
                    </thead>
                    <tbody>
                        {atletas.map((atleta) => {
                            const atletaStats = stats[atleta.id] || { gols: 0, assistencias: 0, amarelo: 0, vermelho: 0 };
                            return (
                                <tr key={atleta.id}>
                                    <td>
                                        <strong>{atleta.nome}</strong><br/>
                                        <span style={{fontSize:'12px', color:'#666'}}>{atleta.posicao}</span>
                                    </td>
                                    <td style={{textAlign: 'center'}}>
                                        <input 
                                            type="number" min="0" className="input-nota"
                                            value={atletaStats.gols}
                                            onChange={(e) => updateStat(atleta.id, 'gols', parseInt(e.target.value))}
                                        />
                                    </td>
                                    <td style={{textAlign: 'center'}}>
                                        <input 
                                            type="number" min="0" className="input-nota"
                                            value={atletaStats.assistencias}
                                            onChange={(e) => updateStat(atleta.id, 'assistencias', parseInt(e.target.value))}
                                        />
                                    </td>
                                    <td style={{textAlign: 'center'}}>
                                        <div style={{display: 'flex', justifyContent: 'center', gap: '10px', alignItems: 'center'}}>
                                            {/* Botão Cartão Amarelo */}
                                            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                                <button 
                                                    onClick={() => handleAddCartao(atleta.id, 'amarelo')}
                                                    style={{
                                                        backgroundColor: '#ffc107', border: '1px solid #d39e00', 
                                                        width: '24px', height: '32px', borderRadius: '3px', cursor: 'pointer'
                                                    }}
                                                    title="Adicionar Cartão Amarelo"
                                                ></button>
                                                <span style={{fontSize: '12px', fontWeight: 'bold'}}>{atletaStats.amarelo}</span>
                                            </div>

                                            {/* Botão Cartão Vermelho */}
                                            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                                <button 
                                                    onClick={() => handleAddCartao(atleta.id, 'vermelho')}
                                                    style={{
                                                        backgroundColor: '#dc3545', border: '1px solid #a71d2a', 
                                                        width: '24px', height: '32px', borderRadius: '3px', cursor: 'pointer'
                                                    }}
                                                    title="Adicionar Cartão Vermelho"
                                                ></button>
                                                <span style={{fontSize: '12px', fontWeight: 'bold'}}>{atletaStats.vermelho}</span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
};