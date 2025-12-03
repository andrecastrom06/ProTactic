import React, { useState, useEffect } from 'react';
import { cartaoService } from '../../../services/cartaoService'; 
import { partidaService } from '../../../services/partidaService'; 
import './AbaAtribuirNotas.css';

// Recebe a nova prop 'aoAtualizarPartida' do pai
export const AbaCartoesInfo = ({ atletas, partidaId, partidaDados, meuClubeId, aoAtualizarPartida }) => {
    const [stats, setStats] = useState({});
    const [loading, setLoading] = useState(false);

    // Estados para o Placar (inicializa com o que veio do banco ou 0)
    const [placarCasa, setPlacarCasa] = useState(0);
    const [placarVisitante, setPlacarVisitante] = useState(0);

    // Atualiza os inputs quando mudar a partida selecionada na tela anterior
    useEffect(() => {
        if (partidaDados) {
            setPlacarCasa(partidaDados.placarClubeCasa || 0);
            setPlacarVisitante(partidaDados.placarClubeVisitante || 0);
        }
    }, [partidaDados]);

    // Lógica para determinar status (Vitória/Derrota/Empate)
    const getStatusResultado = () => {
        if (!partidaDados || !meuClubeId) return null;

        // Verifica se sou Mandante ou Visitante
        const souMandante = (partidaDados.clubeCasaId === meuClubeId) || (partidaDados.clubeCasa?.id === meuClubeId);

        const golsPro = souMandante ? placarCasa : placarVisitante;
        const golsContra = souMandante ? placarVisitante : placarCasa;

        if (golsPro > golsContra) {
            return <span style={{color: '#28a745', fontWeight: 'bold', border: '1px solid #28a745', padding: '4px 8px', borderRadius: '4px'}}>VITÓRIA 🟢</span>;
        } else if (golsPro < golsContra) {
            return <span style={{color: '#dc3545', fontWeight: 'bold', border: '1px solid #dc3545', padding: '4px 8px', borderRadius: '4px'}}>DERROTA 🔴</span>;
        } else {
            return <span style={{color: '#6c757d', fontWeight: 'bold', border: '1px solid #6c757d', padding: '4px 8px', borderRadius: '4px'}}>EMPATE ⚪</span>;
        }
    };

    const handleSalvarPlacar = async () => {
        try {
            setLoading(true);
            await partidaService.atualizarPlacar(partidaId, placarCasa, placarVisitante);
            
            // --- CORREÇÃO AQUI: Chama a função do pai para recarregar os dados ---
            if (aoAtualizarPartida) {
                await aoAtualizarPartida();
            }
            
            alert("Placar atualizado com sucesso!");
        } catch (error) {
            console.error(error);
            alert("Erro ao atualizar placar: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    const updateStat = (id, field, value) => {
        setStats(prev => ({ ...prev, [id]: { ...prev[id], [field]: value } }));
    };

    const handleAddCartao = async (atleta, tipo) => {
        try {
            setLoading(true);
            await cartaoService.registrarCartao(atleta.nome, tipo.toUpperCase()); 
            const currentQtd = stats[atleta.id]?.[tipo] || 0;
            updateStat(atleta.id, tipo, currentQtd + 1);
            alert(`Cartão ${tipo === 'amarelo' ? 'Amarelo' : 'Vermelho'} registrado!`);
        } catch (error) {
            alert("Erro: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleSalvarEstatisticas = async () => {
        try {
            setLoading(true);
            const dadosParaEnviar = atletas.map(atleta => {
                const s = stats[atleta.id] || {};
                return { atletaId: atleta.id, gols: s.gols || 0, assistencias: s.assistencias || 0 };
            });
            await partidaService.registrarEstatisticas(dadosParaEnviar);
            alert("Estatísticas salvas!");
        } catch (error) {
            alert("Erro: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="aba-notas-container">
            
            {/* --- SEÇÃO: PLACAR --- */}
            <div style={{
                backgroundColor: '#f8f9fa', 
                padding: '20px', 
                borderRadius: '8px', 
                marginBottom: '25px',
                border: '1px solid #dee2e6',
                display: 'flex',
                flexDirection: 'column',
                gap: '15px'
            }}>
                <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                    <h3 style={{margin: 0, color: '#444'}}>Resultado da Partida</h3>
                    <div>{getStatusResultado()}</div>
                </div>

                <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '20px'}}>
                    {/* Time Casa */}
                    <div style={{textAlign: 'center'}}>
                        <label style={{display:'block', fontSize:'12px', fontWeight:'bold', color:'#555', marginBottom:'5px'}}>MANDANTE</label>
                        <input 
                            type="number" 
                            min="0"
                            style={{width: '70px', height: '50px', fontSize: '24px', textAlign: 'center', borderRadius: '5px', border: '1px solid #ccc'}}
                            value={placarCasa}
                            onChange={(e) => setPlacarCasa(e.target.value)}
                        />
                    </div>
                    
                    <span style={{fontSize: '32px', fontWeight: 'bold', color: '#999'}}>X</span>

                    {/* Time Visitante */}
                    <div style={{textAlign: 'center'}}>
                        <label style={{display:'block', fontSize:'12px', fontWeight:'bold', color:'#555', marginBottom:'5px'}}>VISITANTE</label>
                        <input 
                            type="number" 
                            min="0"
                            style={{width: '70px', height: '50px', fontSize: '24px', textAlign: 'center', borderRadius: '5px', border: '1px solid #ccc'}}
                            value={placarVisitante}
                            onChange={(e) => setPlacarVisitante(e.target.value)}
                        />
                    </div>

                    <button 
                        className="btn-salvar-notas" 
                        onClick={handleSalvarPlacar}
                        disabled={loading}
                        style={{marginLeft: '30px', padding: '10px 20px', height: 'fit-content'}}
                    >
                        Atualizar Placar
                    </button>
                </div>
            </div>

            <div className="aba-notas-header-actions">
                <h3 style={{margin: 0, color: '#333'}}>Estatísticas dos Atletas</h3>
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