import React, { useState, useEffect, useCallback } from 'react';
import { 
    buscarTodasPropostas, 
    aceitarProposta,      
    recusarProposta       
} from '../../services/propostaService';

import './ListaPropostasPage.css';

// ... (Função getStatusClass e const styles ficam iguais) ...
const getStatusClass = (status) => {
    // ... (igual)
    switch (status) {
        case 'PENDENTE': return 'propostas-status-pendente';
        case 'ACEITE': return 'propostas-status-aceite';
        case 'RECUSADA': return 'propostas-status-rejeitado';
        default: return 'propostas-status-default';
    }
};
const styles = { /* ... (igual) ... */ 
    buttonAprovar: { backgroundColor: '#E0F8E0', color: '#006400', border: '1px solid #006400', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer', marginRight: '5px' },
    buttonRejeitar: { backgroundColor: '#FDE0E0', color: '#A00000', border: '1px solid #A00000', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer' }
};


export const ListaPropostasPage = () => {
    
    // ... (Os teus 'useState', 'useCallback', 'useEffect' e 'handlers' ficam iguais) ...
    const [propostas, setPropostas] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState(null);

    const carregarDados = useCallback(async () => {
        try {
            setCarregando(true);
            setErro(null);
            const dados = await buscarTodasPropostas();
            setPropostas(dados);
        } catch (err) {
            setErro(err.message);
        } finally {
            setCarregando(false);
        }
    }, []); 
    
    useEffect(() => { carregarDados(); }, [carregarDados]); 
    const handleAceitar = async (propostaId) => { /* ... (igual) ... */ 
        if (!window.confirm("Tem a certeza que deseja ACEITAR esta proposta? Esta ação irá transferir o jogador.")) return;
        try { await aceitarProposta(propostaId); alert("Proposta aceite!"); carregarDados(); } 
        catch (err) { alert(`Erro: ${err.message}`); }
    };
    const handleRejeitar = async (propostaId) => { /* ... (igual) ... */ 
        if (!window.confirm("Tem a certeza que deseja REJEITAR esta proposta?")) return;
        try { await recusarProposta(propostaId); alert("Proposta rejeitada!"); carregarDados(); }
        catch (err) { alert(`Erro: ${err.message}`); }
    };

    
    // --- Renderização (HTML/JSX) Atualizada ---
    
    if (carregando) {
        return <div className="propostas-page">Carregando propostas...</div>;
    }
    if (erro) {
        return <div className="propostas-page" style={{ color: 'red' }}>Erro ao carregar dados: {erro}</div>;
    }

    return (
        <div className="propostas-page">
            
            {/* --- (INÍCIO DA MUDANÇA) --- */}
            {/* Adiciona um container para alinhar o título e o botão */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h1>Painel de Propostas</h1>
                
                {/* O botão "+ Nova Proposta" do protótipo */}
                <button 
                    className="propostas-btn propostas-btn-aprovar" 
                    style={{ backgroundColor: '#0056b3', color: 'white', border: 'none' }}
                >
                    + Nova Proposta
                </button>
            </div>
            
            <table className="propostas-table">
                <thead>
                    <tr>
                        {/* Cabeçalhos atualizados para o protótipo */}
                        <th>Atleta</th>
                        <th>Posição</th>
                        <th>Idade</th>
                        <th>Clube Atual</th>
                        <th>Salário Proposto</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {propostas.length === 0 ? (
                        <tr>
                            <td colSpan="7" style={{ textAlign: 'center' }}>Nenhuma proposta encontrada.</td>
                        </tr>
                    ) : (
                        propostas.map((proposta) => (
                            <tr key={proposta.id}>
                                {/* Células atualizadas para usar os novos campos */}
                                <td>{proposta.atletaNome}</td>
                                <td>{proposta.atletaPosicao}</td>
                                <td>{proposta.atletaIdade}</td>
                                <td>{proposta.clubeAtualNome || 'Livre'}</td> {/* Mostra 'Livre' se o clube for nulo */}
                                <td>R$ {proposta.valor.toFixed(2)}</td>
                                <td>
                                    <span className={`propostas-status ${getStatusClass(proposta.status)}`}>
                                        {proposta.status}
                                    </span>
                                </td>
                                <td>
                                    {proposta.status === 'PENDENTE' && (
                                        <>
                                            <button 
                                                className="propostas-btn propostas-btn-aprovar"
                                                onClick={() => handleAceitar(proposta.id)}
                                            >
                                                Aprovar
                                            </button>
                                            <button 
                                                className="propostas-btn propostas-btn-rejeitar"
                                                onClick={() => handleRejeitar(proposta.id)}
                                            >
                                                Rejeitar
                                            </button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
            {/* --- (FIM DA MUDANÇA) --- */}
        </div>
    );
};