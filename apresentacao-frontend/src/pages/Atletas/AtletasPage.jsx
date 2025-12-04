import React, { useState, useEffect, useCallback } from 'react';
import Swal from 'sweetalert2';
import { buscarJogadoresPorClube } from '../../services/jogadorService'; 
import { encerrarLesao } from '../../services/lesaoService';
import { NovoAtletaModal } from '../../components/NovoAtletaModal/NovoAtletaModal';
import { DetalhesAtletaModal } from '../../components/DetalhesAtletaModal/DetalhesAtletaModal';
import { RegistrarLesaoModal } from '../../components/RegistrarLesaoModal/RegistrarLesaoModal';
import { useAuth } from '../../store/AuthContext';
import './AtletasPage.css'; 
import { FaPlus, FaStar, FaEye, FaHeartbeat } from 'react-icons/fa';

const formatarData = (dataString) => {
    if (!dataString) return '-';
    if (Array.isArray(dataString)) {
        dataString = `${dataString[0]}-${String(dataString[1]).padStart(2, '0')}-${String(dataString[2]).padStart(2, '0')}`;
    }
    return new Date(dataString).toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        timeZone: 'UTC'
    });
};

const AtletasPage = () => {
    const [atletas, setAtletas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const [isCadastroModalOpen, setIsCadastroModalOpen] = useState(false);
    const [isLesaoModalOpen, setIsLesaoModalOpen] = useState(false);
    const [isDetalhesModalOpen, setIsDetalhesModalOpen] = useState(false);
    
    const [atletaSelecionado, setAtletaSelecionado] = useState(null);

    const { clubeIdLogado } = useAuth();

    const carregarAtletas = useCallback(async () => {
        if (!clubeIdLogado) return; 

        try {
            setLoading(true);
            const response = await buscarJogadoresPorClube(clubeIdLogado); 
            setAtletas(response); 
            setError(null);
        } catch (err) {
            console.error("Erro ao buscar atletas:", err);
            setError("Não foi possível carregar o elenco.");
            const Toast = Swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000
            });
            Toast.fire({
                icon: 'error',
                title: 'Erro de conexão ao buscar elenco.'
            });

        } finally {
            setLoading(false);
        }
    }, [clubeIdLogado]);

    useEffect(() => {
        carregarAtletas();
    }, [carregarAtletas]); 

    const handleCadastroSuccess = () => {
        setIsCadastroModalOpen(false); 
        carregarAtletas(); 
    };

    const handleAbrirModalLesao = (atleta) => {
        setAtletaSelecionado(atleta);
        setIsLesaoModalOpen(true);
    };

    const handleLesaoSuccess = () => {
        setIsLesaoModalOpen(false);
        setAtletaSelecionado(null);
        carregarAtletas(); 
    };

    const handleAbrirDetalhes = (atleta) => {
        setAtletaSelecionado(atleta);
        setIsDetalhesModalOpen(true);
    };

    const handleEncerrarLesao = async (jogadorId) => {
        const result = await Swal.fire({
            title: 'Encerrar Lesão?',
            text: "O atleta será marcado como 'Saudável' e estará disponível para jogar.",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#28a745',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sim, encerrar',
            cancelButtonText: 'Cancelar'
        });

        if (!result.isConfirmed) return;

        try {
            await encerrarLesao(jogadorId);
            
            await Swal.fire({
                title: 'Recuperado!',
                text: 'Lesão encerrada. O atleta está saudável.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });
            
            carregarAtletas();
        } catch (err) {
            Swal.fire({
                title: 'Erro',
                text: `Erro ao encerrar lesão: ${err.message}`,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };

    if (loading) {
        return <div className="loading-container">Carregando elenco...</div>;
    }

    return (
        <div className="atletas-page-container">
            
            <div className="page-header">
                <h1>Gestão de Atletas</h1>
                <button 
                    className="novo-atleta-btn"
                    onClick={() => setIsCadastroModalOpen(true)} 
                >
                    <FaPlus size={14} />
                    Novo Atleta
                </button>
            </div>

            {error ? (
                 <div className="error-message-container">{error}</div>
            ) : (
                <div className="atletas-list-container">
                    
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
                            
                            const status = atleta.status || 'Indefinido';
                            const saudavel = atleta.saudavel;
                            const grauLesao = atleta.grauLesao;

                            return (
                                <div key={atleta.id} className="atleta-item">
                                    
                                    <span className="nome">
                                        {atleta.nome}
                                        {atleta.capitao && (
                                            <span className="capitao-tag">
                                                <FaStar size={12} /> Capitão
                                            </span>
                                        )}
                                    </span>
                                    <span className="posicao">
                                        {atleta.posicao}
                                    </span>
                                    <span className="idade">
                                        {atleta.idade}
                                    </span>
                                    <span className="status">
                                        <span className={`status-dot ${status === 'Disponível' ? 'disponivel' : 'indisponivel'}`}></span>
                                        {status}
                                    </span>
                                    <span className={`saude ${saudavel ? 'saudavel' : 'lesionado'}`}>
                                        {saudavel ? 'Saudável' : `Lesionado (G${grauLesao})`}
                                    </span>
                                    <span className="contrato">
                                        {formatarData(atleta.chegadaNoClube)}
                                    </span>

                                    <span className="acoes">
                                        <button 
                                            className="action-btn action-btn-details"
                                            onClick={() => handleAbrirDetalhes(atleta)}
                                            title="Ver detalhes"
                                        >
                                            <FaEye size={14} /> Detalhes
                                        </button>
                                        
                                        {saudavel ? (
                                            <button 
                                                className="action-btn action-btn-lesao"
                                                onClick={() => handleAbrirModalLesao(atleta)}
                                                title="Registrar Lesão"
                                            >
                                                <FaHeartbeat size={14} /> Registrar Lesão
                                            </button>
                                        ) : (
                                            <button 
                                                className="action-btn action-btn-details" 
                                                onClick={() => handleEncerrarLesao(atleta.id)}
                                                title="Encerrar Lesão Ativa"
                                                style={{borderColor: '#28a745', color: '#28a745'}} // Destaque visual
                                            >
                                                <FaHeartbeat size={14} /> Encerrar Lesão
                                            </button>
                                        )}
                                    </span>

                                </div>
                            );
                        })
                    ) : (
                        <div className="empty-state">
                            Nenhum atleta encontrado no elenco.
                        </div>
                    )}
                </div>
            )}
            
            {isCadastroModalOpen && (
                <NovoAtletaModal 
                    onClose={() => setIsCadastroModalOpen(false)}
                    onSuccess={handleCadastroSuccess}
                />
            )}

            {isLesaoModalOpen && atletaSelecionado && (
                <RegistrarLesaoModal
                    atleta={atletaSelecionado}
                    onClose={() => setIsLesaoModalOpen(false)}
                    onSuccess={handleLesaoSuccess}
                />
            )}

            {isDetalhesModalOpen && atletaSelecionado && (
                <DetalhesAtletaModal 
                    atleta={atletaSelecionado}
                    onClose={() => setIsDetalhesModalOpen(false)}
                />
            )}
        </div>
    );
};

export default AtletasPage;