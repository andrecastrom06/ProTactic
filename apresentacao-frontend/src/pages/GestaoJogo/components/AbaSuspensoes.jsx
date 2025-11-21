// src/pages/GestaoJogo/components/AbaSuspensoes.jsx
import React, { useState, useEffect } from 'react';
import { API_BASE_URL } from '../../../config/apiConfig'; // Importe sua config
import './AbaAtribuirNotas.css';

export const AbaSuspensoes = ({ clubeId }) => { // Recebe clubeId em vez de lista pronta
    const [suspensos, setSuspensos] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchSuspensoes = async () => {
        if (!clubeId) return;
        try {
            setLoading(true);
            // Chama o novo endpoint criado
            const response = await fetch(`${API_BASE_URL}/registro-cartoes/suspensoes/${clubeId}`);
            if (response.ok) {
                const data = await response.json();
                setSuspensos(data);
            } else {
                console.error("Erro ao buscar suspensões");
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchSuspensoes();
    }, [clubeId]);

    const handleEncerrarSuspensao = async (atletaNome) => {
        if (!window.confirm(`Deseja encerrar a suspensão de ${atletaNome}? Isso limpará os cartões acumulados.`)) {
            return;
        }

        try {
            // Chama o endpoint de limpar cartões, que recalcula a suspensão para false
            const response = await fetch(`${API_BASE_URL}/registro-cartoes/limpar/${atletaNome}`, {
                method: 'POST'
            });

            if (response.ok) {
                alert("Suspensão encerrada com sucesso!");
                fetchSuspensoes(); // Recarrega a lista
            } else {
                alert("Erro ao encerrar suspensão.");
            }
        } catch (error) {
            console.error("Erro:", error);
        }
    };

    return (
        <div className="aba-notas-container">
            <div className="aba-notas-header-actions">
                <h3 style={{margin: 0, color: '#A00000'}}>Jogadores Suspensos do Clube</h3>
                <button onClick={fetchSuspensoes} className="btn-atualizar" style={{marginLeft: '10px', cursor: 'pointer'}}>
                    Atualizar
                </button>
            </div>

            {loading ? (
                <p style={{textAlign: 'center', padding: '20px'}}>Carregando...</p>
            ) : suspensos.length === 0 ? (
                <p style={{textAlign: 'center', color: '#666', padding: '20px'}}>
                    Nenhum jogador suspenso no momento.
                </p>
            ) : (
                <div className="aba-notas-tabela-wrapper">
                    <table className="aba-notas-tabela">
                        <thead>
                            <tr>
                                <th>Atleta</th>
                                <th>Motivo</th>
                                <th>Status</th>
                                <th style={{textAlign: 'center'}}>Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {suspensos.map((s) => {
                                const motivo = s.vermelho > 0 
                                    ? `Cartão Vermelho (${s.vermelho})` 
                                    : `3 Cartões Amarelos (${s.amarelo})`;
                                
                                return (
                                    <tr key={s.id || s.atleta}>
                                        <td><strong>{s.atleta}</strong></td>
                                        <td style={{color: '#d9534f', fontWeight: 'bold'}}>{motivo}</td>
                                        <td>
                                            <span style={{
                                                backgroundColor: '#fde0e0', color: '#A00000', 
                                                padding: '4px 8px', borderRadius: '12px', fontSize: '12px', fontWeight: 'bold'
                                            }}>
                                                SUSPENSO
                                            </span>
                                        </td>
                                        <td style={{textAlign: 'center'}}>
                                            <button 
                                                onClick={() => handleEncerrarSuspensao(s.atleta)}
                                                style={{
                                                    backgroundColor: '#28a745', color: 'white', border: 'none',
                                                    padding: '5px 10px', borderRadius: '4px', cursor: 'pointer', fontSize: '12px'
                                                }}
                                            >
                                                Encerrar Suspensão
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};