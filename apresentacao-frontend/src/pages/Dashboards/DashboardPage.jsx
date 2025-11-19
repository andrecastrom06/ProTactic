import { 
    LuUsers, 
    LuTrophy, 
    LuActivity, 
    LuCircleAlert ,
    LuCalendarClock,
    LuArrowRight
} from "react-icons/lu";
import { FaFutbol } from "react-icons/fa";
import './DashboardPage.css';
import {Link} from 'react-router-dom';
export const DashboardPage = () => {
    const PROXIMO_JOGO = {
        adversario: "Flamengo",
        data: "20/11 - 16:00",
        local: "Maracanã",
        campeonato: "Brasileirão Série A",
        diasRestantes: 2
    };

    const RESUMO_DM = [
        { nome: "Carlos Mendes", lesao: "Estiramento coxa", retorno: "2 semanas" },
        { nome: "Lucas Ferreira", lesao: "Entorse tornozelo", retorno: "5 dias" }
    ];

    const TREINO_HOJE = {
        tipo: "Tático",
        horario: "16:00",
        foco: "Bola parada defensiva",
        local: "CT Campo 1"
    };

    const ALERTA_CONTRATOS = 3; // Contratos vencendo

    return (
        <div className="dashboard-page">
            <header className="dashboard-header">
                <div>
                    <h1>Visão Geral</h1>
                    <p>Bem-vindo ao ProTactic. Aqui está o resumo do dia.</p>
                </div>
                <div className="date-display">
                    <LuCalendarClock /> 19 de Novembro, 2025
                </div>
            </header>

            {/* 1. CARDS DE TOPO (KPIs GERAIS) */}
            <section className="dashboard-stats">
                <div className="stat-card green">
                    <div className="icon-bg"><LuUsers /></div>
                    <div className="stat-content">
                        <span>Elenco Total</span>
                        <strong>28 Atletas</strong>
                    </div>
                </div>
                <div className="stat-card teal">
                    <div className="icon-bg"><LuActivity /></div>
                    <div className="stat-content">
                        <span>Disponíveis</span>
                        <strong>24 Atletas</strong>
                    </div>
                </div>
                <div className="stat-card orange">
                    <div className="icon-bg"><LuCircleAlert /></div>
                    <div className="stat-content">
                        <span>No Dept. Médico</span>
                        <strong>{RESUMO_DM.length} Atletas</strong>
                    </div>
                </div>
                <div className="stat-card red">
                    <div className="icon-bg"><LuTrophy /></div>
                    <div className="stat-content">
                        <span>Jogos na Temp.</span>
                        <strong>34 Jogos</strong>
                    </div>
                </div>
            </section>

            <div className="dashboard-grid">
                
                {/* 2. PRÓXIMO JOGO (DESTAQUE) */}
                <div className="dashboard-widget match-widget">
                    <div className="widget-header">
                        <h3><FaFutbol /> Próximo Confronto</h3>
                        <span className="countdown-badge">{PROXIMO_JOGO.diasRestantes} dias</span>
                    </div>
                    <div className="match-content">
                        <div className="match-teams">
                            <div className="team-home">
                                <div className="logo-placeholder">PRO</div>
                                <span>ProTactic</span>
                            </div>
                            <div className="vs">VS</div>
                            <div className="team-away">
                                <div className="logo-placeholder">FLA</div>
                                <span>{PROXIMO_JOGO.adversario}</span>
                            </div>
                        </div>
                        <div className="match-details">
                            <span>{PROXIMO_JOGO.campeonato}</span>
                            <strong>{PROXIMO_JOGO.data}</strong>
                            <small>{PROXIMO_JOGO.local}</small>
                        </div>
                        <Link to="/jogos" className="btn-preparar">Preparar Escalação <LuArrowRight /></Link>
                    </div>
                </div>

                <div className="center-column">
                    <div className="dashboard-widget">
                        <div className="widget-header">
                            <h3><LuActivity /> Treino de Hoje</h3>
                        </div>
                        <div className="training-summary">
                            <div className="training-time">
                                <strong>{TREINO_HOJE.horario}</strong>
                                <span>{TREINO_HOJE.local}</span>
                            </div>
                            <div className="training-info">
                                <span className="tag-type">{TREINO_HOJE.tipo}</span>
                                <p>Foco: {TREINO_HOJE.foco}</p>
                            </div>
                        </div>
                    </div>

                    {/* Widget Departamento Médico */}
                    <div className="dashboard-widget">
                        <div className="widget-header">
                            <h3><LuCircleAlert /> Departamento Médico</h3>
                        </div>
                        <ul className="dm-list">
                            {RESUMO_DM.map((atleta, idx) => (
                                <li key={idx} className="dm-item">
                                    <div>
                                        <strong>{atleta.nome}</strong>
                                        <span>{atleta.lesao}</span>
                                    </div>
                                    <span className="return-tag">Volta em {atleta.retorno}</span>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

                {/* 4. ALERTA DE CONTRATOS / AVISOS */}
                <div className="dashboard-widget notifications-widget">
                    <div className="widget-header">
                        <h3>Avisos Importantes</h3>
                    </div>
                    <div className="notification-list">
                        <div className="notif-item warning">
                            <div className="notif-icon">⚠️</div>
                            <div className="notif-text">
                                <strong>Contratos Vencendo</strong>
                                <p>{ALERTA_CONTRATOS} atletas encerram contrato em 6 meses.</p>
                            </div>
                        </div>
                        <div className="notif-item info">
                            <div className="notif-icon">ℹ️</div>
                            <div className="notif-text">
                                <strong>Inscrição no Campeonato</strong>
                                <p>Prazo final para inscrição de novos atletas: 25/11.</p>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
};