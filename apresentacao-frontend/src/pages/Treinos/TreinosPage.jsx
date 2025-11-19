import React, { useState } from 'react';
import { 
    LuCalendarDays, LuActivity, LuUsers, LuClock, LuDumbbell,
    LuX, LuCheck, LuTarget, LuZap
} from "react-icons/lu";
import { GiSoccerField } from "react-icons/gi";
import { FaPlus } from "react-icons/fa";
import './TreinosPage.css';

// --- DADOS INICIAIS ---
const DADOS_INICIAIS_SEMANA = [
    { dia: 'Segunda', tipo: 'Físico', hora: '09:00', duracao: '90min', foco: 'Resistência Anaeróbica', intensidade: 85, cor: 'green' },
    { dia: 'Terça', tipo: 'Tático', hora: '10:00', duracao: '120min', foco: 'Construção Ofensiva', intensidade: 70, cor: 'teal' },
    { dia: 'Quarta', tipo: 'Técnico', hora: '09:00', duracao: '90min', foco: 'Finalização', intensidade: 60, cor: 'orange' },
    { dia: 'Quinta', tipo: 'Físico', hora: '08:30', duracao: '90min', foco: 'Força Explosiva', intensidade: 80, cor: 'green' },
    { dia: 'Sexta', tipo: 'Tático', hora: '09:00', duracao: '120min', foco: 'Bola Parada Defensiva', intensidade: 75, cor: 'teal' },
    { dia: 'Sábado', tipo: 'Folga', hora: '', duracao: '', foco: '', intensidade: 0, cor: 'gray' },
    { dia: 'Domingo', tipo: 'Folga', hora: '', duracao: '', foco: '', intensidade: 0, cor: 'gray' }
];

// ... (Mantenha KPI_DATA, CARGA_ATLETAS, HISTORICO_TREINOS iguais) ...
const KPI_DATA = { agendados: 5, cargaMedia: 74, participacao: "25/28", horasTotais: "8.5h" };
const CARGA_ATLETAS = [
    { id: 1, nome: 'João Silva', posicao: 'Atacante', status: 'Disponível', carga: 85, corCarga: 'red' },
    { id: 2, nome: 'Pedro Santos', posicao: 'Meio-campo', status: 'Disponível', carga: 78, corCarga: 'orange' },
    { id: 3, nome: 'Carlos Mendes', posicao: 'Zagueiro', status: 'Lesionado Grau 2', msg: 'Atleta indisponível.', carga: 45, indisponivel: true, corCarga: 'gray' },
    { id: 4, nome: 'Rafael Costa', posicao: 'Goleiro', status: 'Disponível', carga: 90, corCarga: 'red' },
];
const HISTORICO_TREINOS = [
    { data: '15/10/2025', tipo: 'Físico', duracao: '90min', atletas: 24, avaliacao: 'Excelente', corBadge: 'green' },
    { data: '14/10/2025', tipo: 'Tático', duracao: '120min', atletas: 26, avaliacao: 'Muito Bom', corBadge: 'teal' },
];

export const TreinosPage = () => {
    const [agendaSemanal, setAgendaSemanal] = useState(DADOS_INICIAIS_SEMANA);
    const [modalAberto, setModalAberto] = useState(false);
    const [tipoTreinoSelecionado, setTipoTreinoSelecionado] = useState(''); 
    
    // Novo estado com o campo 'foco'
    const [novoTreino, setNovoTreino] = useState({
        dia: 'Segunda',
        hora: '09:00',
        duracao: '90min',
        foco: '',
        intensidade: 50
    });

    const abrirModal = (tipo) => {
        setTipoTreinoSelecionado(tipo);
        setModalAberto(true);
        // Reseta o form ao abrir
        setNovoTreino({ dia: 'Segunda', hora: '09:00', duracao: '90min', foco: '', intensidade: 50 });
    };

    const fecharModal = () => {
        setModalAberto(false);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNovoTreino(prev => ({ ...prev, [name]: value }));
    };

    const handleSalvarTreino = (e) => {
        e.preventDefault();
        let cor = 'gray';
        if (tipoTreinoSelecionado === 'Físico') cor = 'green';
        if (tipoTreinoSelecionado === 'Tático') cor = 'teal';

        const novaAgenda = agendaSemanal.map(item => {
            if (item.dia === novoTreino.dia) {
                return {
                    dia: novoTreino.dia,
                    tipo: tipoTreinoSelecionado,
                    hora: novoTreino.hora,
                    duracao: novoTreino.duracao,
                    foco: novoTreino.foco, // Salvando o foco
                    intensidade: novoTreino.intensidade,
                    cor: cor
                };
            }
            return item;
        });
        setAgendaSemanal(novaAgenda);
        fecharModal();
    };

    const renderStatusBadge = (status) => {
        let className = 'status-badge ';
        if (status.includes('Disponível')) className += 'status-success';
        else if (status.includes('Lesionado')) className += 'status-warning';
        else if (status.includes('Suspenso')) className += 'status-danger';
        return <span className={className}>{status}</span>;
    };

    return (
        <div className="treinos-page">
            {/* --- MODAL DE ADICIONAR TREINO (REDESENHADO) --- */}
            {modalAberto && (
                <div className="modal-overlay" onClick={(e) => { if(e.target.className === 'modal-overlay') fecharModal() }}>
                    <div className="modal-content">
                        {/* Header Dinâmico baseado no tipo */}
                        <div className={`modal-header ${tipoTreinoSelecionado.toLowerCase()}`}>
                            <div className="header-icon-title">
                                {tipoTreinoSelecionado === 'Físico' ? <LuDumbbell size={24}/> : <GiSoccerField size={24}/>}
                                <div>
                                    <h3>Agendar Treino {tipoTreinoSelecionado}</h3>
                                    <span>Defina os detalhes da sessão</span>
                                </div>
                            </div>
                            <button onClick={fecharModal} className="btn-close"><LuX /></button>
                        </div>

                        <form onSubmit={handleSalvarTreino} className="modal-form">
                            <div className="form-section">
                                <label>Dia da Semana</label>
                                <div className="select-wrapper">
                                    <LuCalendarDays className="input-icon" />
                                    <select name="dia" value={novoTreino.dia} onChange={handleInputChange}>
                                        {DADOS_INICIAIS_SEMANA.map(d => <option key={d.dia} value={d.dia}>{d.dia}-feira</option>)}
                                    </select>
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-section">
                                    <label>Horário</label>
                                    <div className="input-wrapper">
                                        <LuClock className="input-icon" />
                                        <input type="time" name="hora" value={novoTreino.hora} onChange={handleInputChange} required />
                                    </div>
                                </div>
                                <div className="form-section">
                                    <label>Duração</label>
                                    <input 
                                        type="text" 
                                        name="duracao" 
                                        value={novoTreino.duracao} 
                                        onChange={handleInputChange} 
                                        placeholder="ex: 90min"
                                        required 
                                    />
                                </div>
                            </div>

                            <div className="form-section">
                                <label>Foco Principal</label>
                                <div className="input-wrapper">
                                    <LuTarget className="input-icon" />
                                    <input 
                                        type="text" 
                                        name="foco" 
                                        value={novoTreino.foco} 
                                        onChange={handleInputChange} 
                                        placeholder={tipoTreinoSelecionado === 'Físico' ? "Ex: Força, Cardio..." : "Ex: Bola Parada, Transição..."}
                                        required 
                                    />
                                </div>
                            </div>

                            <div className="form-section intensity-section">
                                <div className="intensity-header">
                                    <label><LuZap size={14}/> Intensidade Planejada</label>
                                    <span className={`intensity-value ${novoTreino.intensidade > 80 ? 'high' : novoTreino.intensidade > 50 ? 'med' : 'low'}`}>
                                        {novoTreino.intensidade}%
                                    </span>
                                </div>
                                <input 
                                    type="range" 
                                    name="intensidade" 
                                    min="0" max="100" 
                                    className={`range-slider ${tipoTreinoSelecionado.toLowerCase()}`}
                                    value={novoTreino.intensidade} 
                                    onChange={handleInputChange} 
                                />
                                <div className="range-labels">
                                    <span>Baixa</span>
                                    <span>Média</span>
                                    <span>Alta</span>
                                </div>
                            </div>

                            <div className="modal-footer">
                                <button type="button" onClick={fecharModal} className="btn-cancel">Cancelar</button>
                                <button type="submit" className={`btn-save ${tipoTreinoSelecionado.toLowerCase()}`}>
                                    <LuCheck /> Confirmar Agendamento
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <header className="treinos-header">
                <div>
                    <h1>Planejamento de Treinos</h1>
                    <p>Organize e monitore os treinos semanais</p>
                </div>
            </header>

            {/* KPI Grid (Igual) */}
            <section className="kpi-grid">
                {/* ... (Código KPI igual) ... */}
                <div className="kpi-card">
                    <div className="kpi-info">
                        <span>Treinos Agendados</span>
                        <h3>{agendaSemanal.filter(t => t.tipo !== 'Folga').length}</h3>
                        <small>Esta semana</small>
                    </div>
                    <div className="kpi-icon"><LuCalendarDays /></div>
                </div>
                <div className="kpi-card">
                    <div className="kpi-info">
                        <span>Carga Média</span>
                        <h3>{KPI_DATA.cargaMedia}%</h3>
                        <small>Intensidade geral</small>
                    </div>
                    <div className="kpi-icon"><LuActivity /></div>
                </div>
                <div className="kpi-card">
                    <div className="kpi-info">
                        <span>Participação</span>
                        <h3>{KPI_DATA.participacao}</h3>
                        <small>Atletas ativos</small>
                    </div>
                    <div className="kpi-icon"><LuUsers /></div>
                </div>
                <div className="kpi-card">
                    <div className="kpi-info">
                        <span>Horas Totais</span>
                        <h3>{KPI_DATA.horasTotais}</h3>
                        <small>Esta semana</small>
                    </div>
                    <div className="kpi-icon"><LuClock /></div>
                </div>
            </section>

            {/* Calendário Semanal (Atualizado para mostrar o Foco) */}
            <section className="section-container">
                <div className="section-header">
                    <h2>Calendário Semanal</h2>
                    <div className="section-actions">
                        <button className="btn-action primary" onClick={() => abrirModal('Físico')}>
                            <FaPlus /> Treino Físico
                        </button>
                        <button className="btn-action outline" onClick={() => abrirModal('Tático')}>
                            <FaPlus /> Treino Tático
                        </button>
                    </div>
                </div>
                <div className="calendar-grid">
                    {agendaSemanal.map((dia, index) => (
                        <div key={index} className={`calendar-card ${dia.tipo === 'Folga' ? 'folga' : ''}`}>
                            <div className="day-name">{dia.dia}</div>
                            {dia.tipo !== 'Folga' ? (
                                <>
                                    <span className={`type-badge ${dia.cor}`}>{dia.tipo}</span>
                                    <div className="day-details">
                                        <span><LuClock size={12}/> {dia.hora} ({dia.duracao})</span>
                                        {/* Mostra o foco se existir */}
                                        {dia.foco && <span className="day-focus"><LuTarget size={12}/> {dia.foco}</span>}
                                    </div>
                                    <div className="intensity-container-mini">
                                        <div className="intensity-bar-mini">
                                            <div className="fill" style={{width: `${dia.intensidade}%`, background: dia.intensidade > 80 ? '#ef4444' : '#eab308'}}></div>
                                        </div>
                                        <small>{dia.intensidade}%</small>
                                    </div>
                                </>
                            ) : (
                                <span className="no-train">Sem treino</span>
                            )}
                        </div>
                    ))}
                </div>
            </section>

            {/* ... (Resto do código de Carga e Histórico igual) ... */}
            <section className="section-container">
                <div className="section-header"><h2>Carga Semanal por Atleta</h2></div>
                <div className="athletes-list">
                    {CARGA_ATLETAS.map((atleta) => (
                        <div key={atleta.id} className="athlete-load-row">
                            <div className="athlete-info">
                                <span className="athlete-name">{atleta.nome}</span>
                                <span className="athlete-pos">{atleta.posicao}</span>
                                {renderStatusBadge(atleta.status)}
                            </div>
                            <div className="load-bar-container">
                                <div className="load-bar-bg">
                                    <div className="load-bar-fill" style={{ width: `${atleta.carga}%`, backgroundColor: atleta.indisponivel ? '#ccc' : (atleta.carga > 80 ? '#ef4444' : '#eab308') }}></div>
                                </div>
                            </div>
                            <div className="load-value">{atleta.indisponivel ? <span className="unavailable-tag">🚫 Indisponível</span> : <span>{atleta.carga}%</span>}</div>
                        </div>
                    ))}
                </div>
            </section>
            <section className="section-container">
                <div className="section-header"><h2>Histórico de Treinos Realizados</h2></div>
                <div className="history-list">
                    {HISTORICO_TREINOS.map((treino, idx) => (
                        <div key={idx} className="history-item">
                            <div className="history-date"><strong>{treino.data}</strong><span>{treino.tipo}</span></div>
                            <div className="history-meta"><span className="badge-gray">{treino.duracao}</span><span className="badge-users"><LuUsers /> {treino.atletas} atletas</span></div>
                            <div className="history-status"><span className={`status-pill ${treino.corBadge}`}>{treino.avaliacao}</span></div>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
};