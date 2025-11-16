import React, { useState } from 'react';
import { FaStar } from 'react-icons/fa';
import './AbaAtribuirNotas.css';

const StarDisplay = ({ nota }) => {
    const totalStars = 5;
    const filledStars = Math.round(nota / 2); 
    
    return (
        <div>
            {[...Array(totalStars)].map((_, i) => (
                <FaStar 
                    key={i} 
                    color={i < filledStars ? "#ffc107" : "#e4e5e9"} 
                />
            ))}
        </div>
    );
};

export const AbaAtribuirNotas = ({ atletas }) => {
    const [notas, setNotas] = useState({});

    const handleNotaChange = (atletaId, valor) => {
        const notaLimpa = Math.max(0, Math.min(10, parseFloat(valor) || 0));
        
        setNotas(prevNotas => ({
            ...prevNotas,
            [atletaId]: notaLimpa
        }));
    };

    const handleSalvar = () => {
        console.log("Salvando notas:", notas);
        alert("Notas salvas com sucesso!");
    };

    return (
        <div className="aba-notas-container">
            <div className="aba-notas-tabela-wrapper">
                <table className="aba-notas-tabela">
                    <thead>
                        <tr>
                            <th>Nº</th>
                            <th>Atleta</th>
                            <th>Posição</th>
                            <th>Estrelas</th>
                            <th>Nota</th>
                        </tr>
                    </thead>
                    <tbody>
                        {atletas.map((atleta) => {
                            const notaAtual = notas[atleta.id] || 0;
                            return (
                                <tr key={atleta.id}>
                                    <td>{atleta.numero}</td>
                                    <td>{atleta.nome}</td>
                                    <td>{atleta.posicao}</td>
                                    <td>
                                        <StarDisplay nota={notaAtual} />
                                    </td>
                                    <td>
                                        <input 
                                            type="number"
                                            step="0.1"
                                            min="0"
                                            max="10"
                                            className="input-nota"
                                            value={notaAtual || ''}
                                            onChange={(e) => handleNotaChange(atleta.id, e.target.value)}
                                            placeholder="0.0"
                                        />
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>

            <div className="aba-notas-footer">
                <button onClick={handleSalvar} className="btn-salvar-notas">
                    Salvar Avaliações
                </button>
            </div>
        </div>
    );
};