// src/pages/GestaoJogo/components/AbaSuspensoes.jsx
import React from 'react';
import './AbaAtribuirNotas.css';

export const AbaSuspensoes = ({ suspensos, partidaAtualId }) => {
    
    return (
        <div className="aba-notas-container">
            <div className="aba-notas-header-actions">
                <h3 style={{margin: 0, color: '#A00000'}}>Jogadores Suspensos</h3>
            </div>

            {suspensos.length === 0 ? (
                <p style={{textAlign: 'center', color: '#666', padding: '20px'}}>
                    Nenhum jogador suspenso no momento.
                </p>
            ) : (
                <div className="aba-notas-tabela-wrapper">
                    <table className="aba-notas-tabela">
                        <thead>
                            <tr>
                                <th>Atleta</th>
                                <th>Motivo da Suspensão</th>
                                <th>Jogo da Ocorrência</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {suspensos.map((atleta) => {
                                // Simulação da lógica de motivo baseado nos dados (normalmente viria do backend)
                                const motivo = atleta.cartoesVermelhos > 0 ? "Cartão Vermelho Direto" : "Acúmulo de 3 Cartões Amarelos";
                                
                                return (
                                    <tr key={atleta.id}>
                                        <td>
                                            <strong>{atleta.nome}</strong>
                                        </td>
                                        <td style={{color: '#d9534f', fontWeight: 'bold'}}>
                                            {motivo}
                                        </td>
                                        <td>
                                            {/* Aqui você exibiria o ID ou nome do jogo que causou a suspensão */}
                                            Jogo Anterior (Simulado)
                                        </td>
                                        <td>
                                            <span style={{
                                                backgroundColor: '#fde0e0', color: '#A00000', 
                                                padding: '4px 8px', borderRadius: '12px', fontSize: '12px', fontWeight: 'bold'
                                            }}>
                                                SUSPENSO
                                            </span>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
            
            <div style={{marginTop: '20px', padding: '15px', backgroundColor: '#fffbeb', border: '1px solid #fde086', borderRadius: '5px'}}>
                <strong>Regra de Suspensão:</strong> O atleta fica indisponível para escalação caso tenha acumulado 3 cartões amarelos em diferentes jogos ou recebido 1 cartão vermelho.
            </div>
        </div>
    );
};