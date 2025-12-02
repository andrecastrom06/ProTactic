import React, { useState, useEffect } from "react";
import { FaTimes, FaCalendarAlt, FaNotesMedical } from "react-icons/fa";
import { buscarHistoricoLesoes } from "../../services/lesaoService";
import "./DetalhesAtletaModal.css";

// Função auxiliar para formatar datas vindas do Java (Array ou ISO String)
const formatarData = (dataString) => {
  if (!dataString) return "-";
  // Se vier como array [2025, 3, 10] (padrão LocalDate do Java)
  if (Array.isArray(dataString)) {
    return new Date(
      dataString[0],
      dataString[1] - 1,
      dataString[2]
    ).toLocaleDateString("pt-BR");
  }
  // Se vier como String ISO "2025-03-10"
  return new Date(dataString).toLocaleDateString("pt-BR", { timeZone: "UTC" });
};

export const DetalhesAtletaModal = ({ atleta, onClose }) => {
  const [activeTab, setActiveTab] = useState("geral"); // 'geral', 'lesoes', 'estatisticas'

  const [historicoLesoes, setHistoricoLesoes] = useState([]);
  const [loadingLesoes, setLoadingLesoes] = useState(false);

  const statusClass =
    atleta.status === "Disponível"
      ? "status-badge disponivel"
      : atleta.status?.includes("Lesionado")
      ? "status-badge lesionado"
      : "status-badge suspenso";

  useEffect(() => {
    if (activeTab === "lesoes" && atleta?.id) {
      carregarHistorico();
    }
  }, [activeTab, atleta]);

  const carregarHistorico = async () => {
    setLoadingLesoes(true);
    try {
      const dados = await buscarHistoricoLesoes(atleta.id);
      setHistoricoLesoes(dados);
    } catch (error) {
      console.error("Falha ao carregar histórico de lesões:", error);
    } finally {
      setLoadingLesoes(false);
    }
  };

  // Define a cor do card baseado na gravidade (Grau 1 a 5)
  const getGrauClass = (grau) => {
    if (grau >= 3) return "grau-alto"; // Vermelho
    if (grau === 2) return "grau-medio"; // Amarelo/Ocre
    return "grau-baixo"; // Cinza/Verde claro
  };

  return (
    <div
      className="modal-overlay"
      onClick={(e) => {
        if (e.target.className === "modal-overlay") onClose();
      }}
    >
      <div className="detalhes-modal-content">
        {/* --- HEADER --- */}
        <div className="detalhes-header">
          <div>
            <h2>Detalhes do Atleta</h2>
            <p>Informações contratuais, físicas e histórico médico.</p>
          </div>
          <button onClick={onClose} className="btn-close" title="Fechar">
            <FaTimes />
          </button>
        </div>

        {/* --- NAVEGAÇÃO (TABS) --- */}
        <div className="detalhes-tabs">
          <button
            className={`tab-btn ${activeTab === "geral" ? "active" : ""}`}
            onClick={() => setActiveTab("geral")}
          >
            Dados Gerais
          </button>
          <button
            className={`tab-btn ${activeTab === "lesoes" ? "active" : ""}`}
            onClick={() => setActiveTab("lesoes")}
          >
            Histórico de Lesões
          </button>
        </div>

        <div className="detalhes-body">
          {activeTab === "geral" && (
            <div className="dados-gerais-grid">
              <div className="info-group">
                <label>Nome Completo</label>
                <span>{atleta.nome}</span>
              </div>
              <div className="info-group">
                <label>Idade</label>
                <span>{atleta.idade} anos</span>
              </div>

              <div className="info-group">
                <label>Posição</label>
                <span>{atleta.posicao}</span>
              </div>
              <div className="info-group">
                <label>Status Atual</label>
                <div>
                  <span className={statusClass}>
                    {atleta.status || "Indefinido"}
                  </span>
                </div>
              </div>
              <div className="info-group">
                <label>Chegada no Clube</label>
                <span className="data-display">
                  <FaCalendarAlt size={12} />{" "}
                  {formatarData(atleta.chegadaNoClube)}
                </span>
              </div>
            </div>
          )}

          {activeTab === "lesoes" && (
            <div className="lesoes-list">
              {loadingLesoes ? (
                <div className="loading-state">
                  Carregando histórico médico...
                </div>
              ) : historicoLesoes.length === 0 ? (
                <div className="empty-state">
                  <FaNotesMedical size={40} color="#ccc" />
                  <p>Nenhum registro de lesão encontrado para este atleta.</p>
                </div>
              ) : (
                historicoLesoes.map((lesao) => (
                  <div
                    key={lesao.id || Math.random()}
                    className={`lesao-card ${getGrauClass(lesao.grau)}`}
                  >
                    <div className="lesao-header">
                      <span className="lesao-titulo">
                        {lesao.tipo_lesao || lesao.plano || "Lesão Registrada"}
                      </span>
                      <span
                        className={`grau-badge ${getGrauClass(lesao.grau)}`}
                      >
                        Grau {lesao.grau}
                      </span>
                    </div>

                    <div className="lesao-tempo">
                      Volta Esperada:{" "}
                      <strong>{lesao.tempo || "Não informado"}</strong>
                    </div>

                    {lesao.plano && (
                      <div className="lesao-obs">
                        <small>Obs: {lesao.plano}</small>
                      </div>
                    )}
                  </div>
                ))
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
