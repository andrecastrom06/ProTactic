import React, { useState, useEffect } from 'react';
import { FaStar } from 'react-icons/fa';
import './AbaAtribuirNotas.css';

const StarDisplay = ({ nota }) => {
    const totalStars = 5;
    const filledStars = Math.round(nota / 2); 
    return (
        <div className="star-display">
            {[...Array(totalStars)].map((_, i) => (
                <FaStar key={i} color={i < filledStars ? "#ffc107" : "#e4e5e9"} />
            ))}
        </div>
    );
};

export const AbaAtribuirNotas = ({ atletas, onSalvar, notasIniciais }) => {
    const [avaliacoes, setAvaliacoes] = useState({});

    // Carrega as notas vindas do banco (notasIniciais) para o estado local
    useEffect(() => {
        if (notasIniciais && notasIniciais.length > 0) {
            const mapaNotas = {};
            notasIniciais.forEach(n => {
                // O backend retorna { jogadorId: "...", nota: X, observacao: "..." }
                // Vamos mapear para o formato que o estado espera
                mapaNotas[parseInt(n.jogadorId)] = {
                    nota: n.nota || 0,
                    observacao: n.observacao || ''
                };
            });
            setAvaliacoes(mapaNotas);
        }
    }, [notasIniciais]);

    const handleNotaChange = (atletaId, valor) => {
        const notaLimpa = Math.max(0, Math.min(10, parseFloat(valor) || 0));
        setAvaliacoes(prev => ({
            ...prev,
            [atletaId]: { ...prev[atletaId], nota: notaLimpa }
        }));
    };

    const handleObsChange = (atletaId, texto) => {
        setAvaliacoes(prev => ({
            ...prev,
            [atletaId]: { ...prev[atletaId], observacao: texto }
        }));
    };

    const handleSalvarClick = () => {
        if (onSalvar) onSalvar(avaliacoes);
    };

    return (
        <div className="aba-notas-container">
            {/* Botão movido para o topo */}
            <div className="aba-notas-header-actions">
                <button onClick={handleSalvarClick} className="btn-salvar-notas">
                    Salvar Avaliações
                </button>
            </div>

            <div className="aba-notas-tabela-wrapper">
                <table className="aba-notas-tabela">
                    <thead>
                        <tr>
                            <th style={{width: '30%'}}>Atleta</th>
                            <th style={{width: '15%'}}>Nota (0-10)</th>
                            <th style={{width: '15%'}}>Visual</th>
                            <th style={{width: '40%'}}>Observação</th>
                        </tr>
                    </thead>
                    <tbody>
                        {atletas.map((atleta) => {
                            const dados = avaliacoes[atleta.id] || { nota: 0, observacao: '' };
                            return (
                                <tr key={atleta.id}>
                                    <td>
                                        <strong>{atleta.nome}</strong><br/>
                                        <span style={{fontSize:'12px', color:'#666'}}>{atleta.posicao}</span>
                                    </td>
                                    <td>
                                        <input 
                                            type="number" step="0.5" min="0" max="10"
                                            className="input-nota"
                                            value={dados.nota || ''}
                                            onChange={(e) => handleNotaChange(atleta.id, e.target.value)}
                                            placeholder="0"
                                        />
                                    </td>
                                    <td><StarDisplay nota={dados.nota || 0} /></td>
                                    <td>
                                        <input 
                                            type="text"
                                            className="input-obs"
                                            value={dados.observacao || ''}
                                            onChange={(e) => handleObsChange(atleta.id, e.target.value)}
                                            placeholder="Observação..."
                                        />
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