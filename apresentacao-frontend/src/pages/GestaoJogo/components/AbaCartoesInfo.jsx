// src/pages/GestaoJogo/components/AbaCartoesInfo.jsx
import React, { useState, useEffect } from 'react';
import { cartaoService } from '../../../services/cartaoService'; 
import { partidaService } from '../../../services/partidaService'; // Importe o serviço de partida
import './AbaAtribuirNotas.css';

export const AbaCartoesInfo = ({ atletas, partidaId }) => {
    const [stats, setStats] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const carregarDados = async () => {
            try {
                // Lógica futura para carregar dados já salvos, se necessário
            } catch (error) {
                console.error("Erro ao carregar dados iniciais", error);
            }
        };
        carregarDados();
    }, [atletas]);

    const updateStat = (id, field, value) => {
        setStats(prev => ({
            ...prev,
            [id]: {
                ...prev[id],
                [field]: value
            }
        }));
    };

    // Função para registrar CARTÃO (imediato)
    const handleAddCartao = async (atleta, tipo) => {
        try {
            setLoading(true);
            // Envia o NOME do atleta, conforme exigido pelo backend
            await cartaoService.registrarCartao(atleta.nome, tipo.toUpperCase()); 

            const currentQtd = stats[atleta.id]?.[tipo] || 0;
            updateStat(atleta.id, tipo, currentQtd + 1);

            alert(`Cartão ${tipo === 'amarelo' ? 'Amarelo' : 'Vermelho'} registrado com sucesso!`);
        } catch (error) {
            console.error(error);
            alert("Erro ao registrar cartão: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    // Função para salvar ESTATÍSTICAS (Gols, Assistências, Jogos)
    const handleSalvarEstatisticas = async () => {
        try {
            setLoading(true);
            
            // Prepara o payload: ID do atleta, Gols e Assistências
            // O backend deve incrementar 'jogos' automaticamente para cada item dessa lista
            const dadosParaEnviar = atletas.map(atleta => {
                const s = stats[atleta.id] || {};
                return {
                    atletaId: atleta.id,
                    gols: s.gols || 0,
                    assistencias: s.assistencias || 0
                };
            });

            // Chama o novo endpoint do backend
            await partidaService.registrarEstatisticas(dadosParaEnviar);
            
            alert("Estatísticas salvas com sucesso! (Jogos, Gols e Assistências atualizados)");
        } catch (error) {
            console.error(error);
            alert("Erro ao salvar estatísticas: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="aba-notas-container">
            <div className="aba-notas-header-actions">
                <h3 style={{margin: 0, color: '#333'}}>Registro de Eventos da Partida</h3>
                {/* Botão conectado à função de salvar */}
                <button 
                    className="btn-salvar-notas" 
                    onClick={handleSalvarEstatisticas}
                    disabled={loading}
                    style={{ opacity: loading ? 0.7 : 1 }}
                >
                    {loading ? 'Salvando...' : 'Salvar Estatísticas'}
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
                                            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                                <button 
                                                    disabled={loading}
                                                    onClick={() => handleAddCartao(atleta, 'amarelo')}
                                                    style={{
                                                        backgroundColor: '#ffc107', border: '1px solid #d39e00', 
                                                        width: '24px', height: '32px', borderRadius: '3px', cursor: 'pointer',
                                                        opacity: loading ? 0.5 : 1
                                                    }}
                                                    title="Adicionar Cartão Amarelo"
                                                ></button>
                                                <span style={{fontSize: '12px', fontWeight: 'bold'}}>{atletaStats.amarelo}</span>
                                            </div>

                                            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                                <button 
                                                    disabled={loading}
                                                    onClick={() => handleAddCartao(atleta, 'vermelho')}
                                                    style={{
                                                        backgroundColor: '#dc3545', border: '1px solid #a71d2a', 
                                                        width: '24px', height: '32px', borderRadius: '3px', cursor: 'pointer',
                                                        opacity: loading ? 0.5 : 1
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