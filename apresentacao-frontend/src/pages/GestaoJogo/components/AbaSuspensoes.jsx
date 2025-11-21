// src/pages/GestaoJogo/components/AbaSuspensoes.jsx
import React, { useState, useEffect } from 'react';
import { cartaoService } from '../../../services/cartaoService';
import './AbaAtribuirNotas.css';

export const AbaSuspensoes = ({ atletas }) => { // Recebe a lista de atletas do time
    const [suspensos, setSuspensos] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const verificarSuspensoes = async () => {
            if (!atletas || atletas.length === 0) return;

            try {
                setLoading(true);
                const listaSuspensosCalculada = [];

                // Como o backend não tem endpoint de suspensão, iteramos (ou buscamos todos os cartões)
                // Idealmente, o backend deveria ter um endpoint /suspensoes
                for (const atleta of atletas) {
                    const cartoes = await cartaoService.listarPorAtleta(atleta.id); //
                    
                    // Lógica de Negócio: 3 Amarelos ou 1 Vermelho
                    const amarelos = cartoes.filter(c => c.tipo && c.tipo.toUpperCase() === 'AMARELO').length;
                    const vermelhos = cartoes.filter(c => c.tipo && c.tipo.toUpperCase() === 'VERMELHO').length;

                    // Verifica regra simples de suspensão (adaptar conforme regra do campeonato)
                    // Exemplo: A cada 3 amarelos suspende, ou 1 vermelho direto
                    // Nota: O backend retorna uma lista acumulada. Precisamos saber se o jogador JÁ CUMPRIU a suspensão.
                    // Como o backend atual é simples, vamos assumir que se tem 3 amarelos "ativos", está suspenso.
                    
                    // ATENÇÃO: Para um sistema real, o backend precisaria informar o "saldo" de cartões.
                    // Aqui faremos uma verificação básica baseada no retorno bruto.
                    if (amarelos >= 3 || vermelhos >= 1) {
                        listaSuspensosCalculada.push({
                            ...atleta,
                            motivo: vermelhos > 0 ? "Cartão Vermelho" : "3 Cartões Amarelos",
                            qtdAmarelos: amarelos,
                            qtdVermelhos: vermelhos
                        });
                    }
                }
                setSuspensos(listaSuspensosCalculada);
            } catch (error) {
                console.error("Erro ao calcular suspensões:", error);
            } finally {
                setLoading(false);
            }
        };

        verificarSuspensoes();
    }, [atletas]);
    
    if (loading) return <p style={{textAlign:'center', padding: '20px'}}>Verificando suspensões...</p>;

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
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {suspensos.map((atleta) => (
                                <tr key={atleta.id}>
                                    <td>
                                        <strong>{atleta.nome}</strong>
                                    </td>
                                    <td style={{color: '#d9534f', fontWeight: 'bold'}}>
                                        {atleta.motivo} ({atleta.qtdAmarelos} CA, {atleta.qtdVermelhos} CV)
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
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};