import React, { useState, useEffect } from 'react';
import { useAuth } from '../../../store/AuthContext'; // Ajuste o caminho conforme sua estrutura
import { buscarEscalacaoPorPartida } from '../../../services/jogoService';
import './AbaRelatorioDesempenho.css'; // Vamos criar este CSS abaixo

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { FaFilePdf } from 'react-icons/fa';

import './AbaRelatorioDesempenho.css';

export const AbaRelatorioDesempenho = ({ partidaId, todosAtletas, partidas, notasDoBanco }) => {
    const { clubeIdLogado } = useAuth();
    const [escalacao, setEscalacao] = useState(null);
    const [loading, setLoading] = useState(false);
    
    const notasDaPartida = notasDoBanco.filter(nota => nota.jogoId === `JOGO-${partidaId}`);
    const detalhesPartida = partidas.find(p => String(p.id) === String(partidaId));
    
    const atletasMap = todosAtletas.reduce((acc, atleta) => {
        acc[atleta.id] = atleta;
        return acc;
    }, {});

    useEffect(() => {
        const carregarRelatorio = async () => {
            if (!partidaId || !clubeIdLogado) {
                setEscalacao(null);
                return;
            }
            setLoading(true);
            try {
                const escalacaoData = await buscarEscalacaoPorPartida(partidaId, clubeIdLogado);
                setEscalacao(escalacaoData);
            } catch (error) {
                console.error("Erro ao carregar relatório:", error);
                setEscalacao(null);
            } finally {
                setLoading(false);
            }
        };
        carregarRelatorio();
    }, [partidaId, clubeIdLogado]);

    const getNotaInfo = (jogadorId) => {
        const notaObj = notasDaPartida.find(n => 
            n.jogoId === `JOGO-${partidaId}` && String(n.jogadorId) === String(jogadorId)
        );
        if (!notaObj) return { nota: '-', obs: '-' };
        return { 
            nota: typeof notaObj.nota === 'number' ? notaObj.nota.toFixed(1) : notaObj.nota, 
            obs: notaObj.observacao || '-' 
        };
    };

    const getCartoes = (jogadorId) => {
        const jogador = atletasMap[jogadorId];
        return {
            amarelos: jogador ? jogador.cartoesAmarelos : 0, 
            vermelhos: jogador ? jogador.cartoesVermelhos : 0
        };
    };

    const getJogadoresRelacionados = () => {
        if (!escalacao) return [];
        const jogadores = [];
        for(let i = 1; i <= 11; i++) {
            const idKey = `idJogador${i}`;
            const id = escalacao[idKey];
            if (id && atletasMap[id]) {
                jogadores.push({ 
                    ...atletasMap[id], 
                    tipo: 'Titular', 
                    notaInfo: getNotaInfo(id) 
                });
            }
        }
        return jogadores;
    }

    const handleDownloadPDF = () => {
        const doc = new jsPDF();

        // Título e Cabeçalho
        doc.setFontSize(18);
        doc.setTextColor(0, 77, 64); // Verde Escuro
        doc.text(`Relatório de Desempenho - Partida #${partidaId}`, 14, 22);

        // Dados do Jogo
        doc.setFontSize(12);
        doc.setTextColor(0, 0, 0);
        const dataJogo = detalhesPartida?.dataJogo ? new Date(detalhesPartida.dataJogo).toLocaleDateString() : 'N/A';
        const adversario = detalhesPartida?.adversario || 'N/A';
        const placar = `${detalhesPartida?.placarClubeCasa ?? '-'} x ${detalhesPartida?.placarClubeVisitante ?? '-'}`;
        const esquema = escalacao?.esquema || 'N/A';

        doc.text(`Data: ${dataJogo}`, 14, 32);
        doc.text(`Adversário: ${adversario}`, 14, 39);
        doc.text(`Placar: ${placar}`, 14, 46);
        doc.text(`Esquema Tático: ${esquema}`, 14, 53);

        // Preparar dados para a Tabela
        const dadosTabela = getJogadoresRelacionados().map(jogador => {
            const cartoes = getCartoes(jogador.id);
            let textoCartoes = '';
            if(cartoes.amarelos > 0) textoCartoes += `A: ${cartoes.amarelos} `;
            if(cartoes.vermelhos > 0) textoCartoes += `V: ${cartoes.vermelhos}`;
            if(!textoCartoes) textoCartoes = '-';

            return [
                jogador.nome,
                jogador.posicao,
                jogador.notaInfo.nota,
                textoCartoes,
                jogador.notaInfo.obs
            ];
        });

        autoTable(doc, {
            startY: 60,
            head: [['Atleta', 'Posição', 'Nota', 'Cartões', 'Observações']],
            body: dadosTabela,
            headStyles: { fillColor: [0, 77, 64] }, // Verde do cabeçalho
            alternateRowStyles: { fillColor: [240, 240, 240] }, 
            styles: { fontSize: 10 },
            columnStyles: {
                0: { cellWidth: 40 },
                4: { cellWidth: 'auto' }
            }
        });

        const pageCount = doc.internal.getNumberOfPages();
        for(let i = 1; i <= pageCount; i++) {
            doc.setPage(i);
            doc.setFontSize(10);
            doc.text('Gerado pelo Sistema ProTactic', 14, doc.internal.pageSize.height - 10);
        }

        doc.save(`relatorio_partida_${partidaId}.pdf`);
    };


    if (!partidaId) return <div className="relatorio-vazio"><p>👈 Selecione uma partida...</p></div>;
    if (loading) return <div className="relatorio-loading">Carregando dados...</div>;

    const linhasTabela = getJogadoresRelacionados();

    return (
        <div className="relatorio-desempenho-container">
            
            <div className="relatorio-header">
                <div className="header-left">
                    <h3>📄 Relatório de Desempenho</h3>
                    <span className="partida-id">Partida #{partidaId}</span>
                </div>
                
                <button className="btn-pdf" onClick={handleDownloadPDF}>
                    <FaFilePdf /> Baixar PDF
                </button>
            </div>

            <div className="resumo-jogo-card">
                <div className="resumo-item">
                    <span className="label">Data</span>
                    <strong>{detalhesPartida?.dataJogo ? new Date(detalhesPartida.dataJogo).toLocaleDateString() : 'N/A'}</strong>
                </div>
                <div className="resumo-item">
                    <span className="label">Adversário</span>
                    <strong>{detalhesPartida?.adversario || 'Não informado'}</strong>
                </div>
                <div className="resumo-item">
                    <span className="label">Placar</span>
                    <strong className="placar">
                        {detalhesPartida?.placarClubeCasa ?? '-'} x {detalhesPartida?.placarClubeVisitante ?? '-'}
                    </strong>
                </div>
                <div className="resumo-item">
                    <span className="label">Esquema Tático</span>
                    <strong>{escalacao?.esquema || 'N/A'}</strong>
                </div>
            </div>

            <div className="tabela-container">
                <h4>Escalação e Avaliações</h4>
                {linhasTabela.length > 0 ? (
                    <table className="tabela-relatorio">
                        <thead>
                            <tr>
                                <th>Atleta</th>
                                <th>Posição</th>
                                <th style={{textAlign: 'center'}}>Nota</th>
                                <th>Cartões</th>
                                <th>Observações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {linhasTabela.map((linha) => {
                                const cartoes = getCartoes(linha.id);
                                return (
                                    <tr key={linha.id}>
                                        <td>{linha.nome}</td>
                                        <td>{linha.posicao}</td>
                                        <td style={{textAlign: 'center', fontWeight: 'bold'}}>
                                            {linha.notaInfo.nota !== '-' ? (
                                                <span className={`nota-badge ${parseFloat(linha.notaInfo.nota) >= 7 ? 'boa' : parseFloat(linha.notaInfo.nota) < 5 ? 'ruim' : 'media'}`}>
                                                    {linha.notaInfo.nota}
                                                </span>
                                            ) : '-'}
                                        </td>
                                        <td>
                                            {cartoes.amarelos > 0 && <span title="Amarelo">🟨 {cartoes.amarelos > 1 ? `x${cartoes.amarelos}` : ''}</span>}
                                            {cartoes.vermelhos > 0 && <span title="Vermelho">🟥</span>}
                                            {cartoes.amarelos === 0 && cartoes.vermelhos === 0 && <span style={{color: '#ccc'}}>-</span>}
                                        </td>
                                        <td className="coluna-obs">{linha.notaInfo.obs}</td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                ) : (
                    <div className="aviso-sem-dados">
                        <p>Nenhuma escalação salva para esta partida.</p>
                    </div>
                )}
            </div>
        </div>
    );
};