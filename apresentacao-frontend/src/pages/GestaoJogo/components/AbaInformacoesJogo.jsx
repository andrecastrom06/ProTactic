import React, { useState, useEffect } from 'react';
import './AbaInformacoesJogo.css';

export const AbaInformacoesJogo = ({ atletas, dadosIniciais, onSalvar }) => {
    // Estado para armazenar gols, assistências e cartões por ID do atleta
    const [estatisticas, setEstatisticas] = useState({});

    // Carrega dados iniciais se existirem (vindo do banco/pai)
    useEffect(() => {
        if (dadosIniciais) {
            setEstatisticas(dadosIniciais);
        }
    }, [dadosIniciais]);

    const handleChange = (atletaId, campo, valor) => {
        setEstatisticas(prev => ({
            ...prev,
            [atletaId]: {
                ...prev[atletaId],
                [campo]: valor
            }
        }));
    };

    const handleSalvar = () => {
        if (onSalvar) onSalvar(estatisticas);
    };

    return (
        <div className="aba-info-jogo-container">
            <div className="aba-info-header">
                <p className="info-instrucao">
                    Preencha as estatísticas da partida para os jogadores escalados.
                </p>
                <button onClick={handleSalvar} className="btn-salvar-info">
                    Salvar Informações
                </button>
            </div>

            <div className="tabela-wrapper">
                <table className="tabela-info-jogo">
                    <thead>
                        <tr>
                            <th style={{width: '30%'}}>Atleta</th>
                            <th style={{width: '15%', textAlign: 'center'}}>⚽ Gols</th>
                            <th style={{width: '15%', textAlign: 'center'}}>👟 Assist.</th>
                            <th style={{width: '20%', textAlign: 'center'}}>🟨🟥 Cartões</th>
                        </tr>
                    </thead>
                    <tbody>
                        {atletas.map(atleta => {
                            const stats = estatisticas[atleta.id] || { gols: 0, assistencias: 0, cartao: 'NENHUM' };
                            
                            return (
                                <tr key={atleta.id}>
                                    <td>
                                        <div className="atleta-info">
                                            <span className="nome">{atleta.nome}</span>
                                            <span className="posicao">{atleta.posicao}</span>
                                        </div>
                                    </td>
                                    <td align="center">
                                        <input 
                                            type="number" 
                                            min="0" 
                                            className="input-numero"
                                            value={stats.gols}
                                            onChange={(e) => handleChange(atleta.id, 'gols', parseInt(e.target.value) || 0)}
                                        />
                                    </td>
                                    <td align="center">
                                        <input 
                                            type="number" 
                                            min="0" 
                                            className="input-numero"
                                            value={stats.assistencias}
                                            onChange={(e) => handleChange(atleta.id, 'assistencias', parseInt(e.target.value) || 0)}
                                        />
                                    </td>
                                    <td align="center">
                                        <select 
                                            className={`select-cartao ${stats.cartao}`}
                                            value={stats.cartao || 'NENHUM'}
                                            onChange={(e) => handleChange(atleta.id, 'cartao', e.target.value)}
                                        >
                                            <option value="NENHUM">Nenhum</option>
                                            <option value="AMARELO">Amarelo</option>
                                            <option value="VERMELHO">Vermelho</option>
                                            <option value="AMBOS">Amarelo + Vermelho</option>
                                        </select>
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