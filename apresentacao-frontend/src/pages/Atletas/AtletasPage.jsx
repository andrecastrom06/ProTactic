import React, { useState, useEffect, useCallback } from 'react';
import { buscarTodosJogadores } from '../../services/jogadorService';
import './AtletasPage.css'; // (O teu CSS)
import { FaPlus, FaStar, FaEye, FaHeartbeat } from 'react-icons/fa';

// Importa o novo Modal e o hook de autenticação
import { NovoAtletaModal } from '../../components/NovoAtletaModal/NovoAtletaModal';
import { useAuth } from '../../store/AuthContext'; 

/**
 * Função para formatar a data (Corrigida para LocalDate)
 */
const formatarData = (dataString) => {
    if (!dataString) return '-';
    // O backend envia um array [ano, mes, dia], vamos converter
    if (Array.isArray(dataString)) {
        dataString = `${dataString[0]}-${String(dataString[1]).padStart(2, '0')}-${String(dataString[2]).padStart(2, '0')}`;
    }
    return new Date(dataString).toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        timeZone: 'UTC' // Importante para datas sem hora
    });
};


const AtletasPage = () => {
    const [atletas, setAtletas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Estado para controlar o modal
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    // Pega o ID do clube logado (para passar ao modal)
    const { clubeIdLogado } = useAuth();

    // Usa o useCallback para a função de carregar
    const carregarAtletas = useCallback(async () => {
        try {
            setLoading(true);
            const response = await buscarTodosJogadores();
            setAtletas(response); 
            setError(null);
        } catch (err) {
            console.error("Erro ao buscar atletas:", err);
            setError("Não foi possível carregar os atletas.");
        } finally {
            setLoading(false);
        }
    }, []); // Dependência vazia

    useEffect(() => {
        carregarAtletas();
    }, [carregarAtletas]); 

    // Função de sucesso (chamada pelo modal)
    const handleCadastroSuccess = () => {
        setIsModalOpen(false); // Fecha o modal
        carregarAtletas(); // Recarrega a lista de atletas
    };

    if (loading) {
        return <div style={{ padding: '20px' }}>Carregando atletas...</div>;
    }

    if (error) {
        return <div style={{ padding: '20px' }}>{error}</div>;
    }

    return (
        <div className="atletas-page-container">
            
            <div className="page-header">
                <h1>Gestão de Atletas</h1>
                {/* Liga o botão para abrir o modal */}
                <button 
                    className="novo-atleta-btn"
                    onClick={() => setIsModalOpen(true)}
                >
                    <FaPlus size={14} />
                    Novo Atleta
                </button>
            </div>

            <div className="atletas-list-container">
                
                {/* Cabeçalho da Lista */}
                <div className="atleta-item header">
                    <span className="nome">Nome</span>
                    <span className="posicao">Posição</span>
                    <span className="idade">Idade</span>
                    <span className="status">Status</span>
                    <span className="saude">Saúde</span>
                    <span className="contrato">Contrato (Início)</span>
                    <span className="acoes">Ações</span>
                </div>

                {atletas.length > 0 ? (
                    atletas.map((atleta) => {
                        
                        // --- (INÍCIO DAS CORREÇÕES) ---
                        // 1. Os dados agora vêm direto da API
                        const status = atleta.status || 'Indefinido';
                        const saudavel = atleta.saudavel; // Vem como true/false
                        const grauLesao = atleta.grauLesao;
                        // --- (FIM DAS CORREÇÕES) ---

                        return (
                            <div key={atleta.id} className="atleta-item">
                                
                                {/* Nome + Tag de Capitão */}
                                <span className="nome">
                                    {atleta.nome}
                                    {/* A API agora envia 'capitao' (era 'isCapitao') */}
                                    {atleta.capitao && (
                                        <span className="capitao-tag">
                                            <FaStar size={12} /> Capitão
                                        </span>
                                    )}
                                </span>

                                {/* Posição */}
                                <span className="posicao">
                                    {atleta.posicao}
                                </span>

                                {/* Idade (API agora envia 'idade') */}
                                <span className="idade">
                                    {atleta.idade}
                                </span>

                                {/* Status (API agora envia 'status') */}
                                <span className="status">
                                    <span className={`status-dot ${status === 'Disponível' ? 'disponivel' : 'indisponivel'}`}></span>
                                    {status}
                                </span>

                                {/* Saúde (API agora envia 'saudavel' e 'grauLesao') */}
                                <span className={`saude ${saudavel ? 'saudavel' : 'lesionado'}`}>
                                    {saudavel ? 'Saudável' : `Lesionado (G${grauLesao})`}
                                </span>

                                {/* Contrato (API agora envia 'chegadaNoClube') */}
                                <span className="contrato">
                                    {formatarData(atleta.chegadaNoClube)}
                                </span>

                                {/* Ações */}
                                <span className="acoes">
                                    <button className="action-btn action-btn-details">
                                        <FaEye size={14} /> Detalhes
                                    </button>
                                    <button className="action-btn action-btn-lesao">
                                        <FaHeartbeat size={14} /> Lesão
                                    </button>
                                </span>

                            </div>
                        );
                    })
                ) : (
                    <div style={{ padding: '20px', textAlign: 'center' }}>
                        Nenhum atleta cadastrado.
                    </div>
                )}
            </div>
            
            {/* Renderiza o Modal (se isModalOpen for true) */}
            {isModalOpen && (
                <NovoAtletaModal 
                    onClose={() => setIsModalOpen(false)}
                    onSuccess={handleCadastroSuccess}
                />
            )}

        </div>
    );
};

export default AtletasPage;