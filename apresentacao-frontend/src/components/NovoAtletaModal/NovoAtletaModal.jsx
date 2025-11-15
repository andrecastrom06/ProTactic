import React, { useState, useMemo } from 'react';
import { useAuth } from '../../store/AuthContext';
import { cadastrarNovoAtleta } from '../../services/jogadorService';
import './NovoAtletaModal.css'; // Usaremos os mesmos estilos do outro modal

// Listas de opções (como no teu protótipo)
const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];
const SITUACOES = ["Com contrato ativo", "Livre", "Emprestado"];

export const NovoAtletaModal = ({ onClose, onSuccess }) => {
    const { clubeIdLogado } = useAuth(); // Pega o ID do nosso clube

    // Estado do formulário
    const [nome, setNome] = useState('');
    const [posicao, setPosicao] = useState('');
    const [idade, setIdade] = useState('');
    const [validadeContrato, setValidadeContrato] = useState('');
    const [situacao, setSituacao] = useState('ATIVO');
    const [erroApi, setErroApi] = useState(null);

    // --- Lógica dos Requisitos (Pedido 4) ---
    const requisitos = useMemo(() => {
        return {
            nomePreenchido: nome.trim().length > 2,
            posicaoSelecionada: POSICOES.includes(posicao),
            idadeValida: parseInt(idade, 10) > 15,
            situacaoValida: true, // (Pode adicionar regras aqui)
        };
    }, [nome, posicao, idade]);

    // Verifica se todos os requisitos estão "true"
    const isFormularioValido = Object.values(requisitos).every(Boolean);

    // Handler para o envio
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!isFormularioValido) {
            alert("Por favor, preencha todos os requisitos.");
            return;
        }

        try {
            const formulario = {
                nomeCompleto: nome,
                posicao: posicao,
                idade: parseInt(idade, 10),
                validadeDoContrato: validadeContrato,
                situacaoContratual: situacao,
                clubeId: clubeIdLogado // Associa ao clube do usuário logado
            };

            await cadastrarNovoAtleta(formulario);
            alert("Atleta cadastrado com sucesso!");
            onSuccess(); // Fecha o modal e refresca a lista de atletas
            
        } catch (error) {
            setErroApi(error.message);
        }
    };

    // Componente auxiliar para os requisitos
    const RequisitoItem = ({ label, valido }) => (
        <li className={`requisito-item ${valido ? 'valido' : 'invalido'}`}>
            <span>{valido ? '✔' : '✖'}</span> {label}
        </li>
    );

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Cadastrar Novo Atleta</h2>
                    <button onClick={onClose} className="modal-close-btn">&times;</button>
                </div>
                
                <p className="modal-subtitle">Preencha os dados do novo atleta para cadastro no elenco.</p>

                {/* Caixa de Aviso (como no protótipo) */}
                <div className="modal-alert">
                    Cadastro restrito fora da janela de transferências. Marque 'Atleta Livre' se não possuir contrato.
                </div>

                <form className="modal-form" onSubmit={handleSubmit}>
                    
                    <div className="form-group-row">
                        <div className="form-group" style={{flex: 2}}>
                            <label>Nome Completo *</label>
                            <input type="text" value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Ex: João Silva" />
                        </div>
                        <div className="form-group" style={{flex: 1}}>
                            <label>Idade *</label>
                            <input type="number" value={idade} onChange={(e) => setIdade(e.target.value)} placeholder="Ex: 24" />
                        </div>
                    </div>

                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Posição *</label>
                            <select value={posicao} onChange={(e) => setPosicao(e.target.value)}>
                                <option value="">Selecione</option>
                                {POSICOES.map(p => <option key={p} value={p}>{p}</option>)}
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Situação Contratual *</label>
                            <select value={situacao} onChange={(e) => setSituacao(e.target.value)}>
                                {SITUACOES.map(s => <option key={s} value={s}>{s}</option>)}
                            </select>
                        </div>
                    </div>
                    
                    <div className="form-group">
                        <label>Validade do Contrato</label>
                        <input type="date" value={validadeContrato} onChange={(e) => setValidadeContrato(e.target.value)} />
                    </div>

                    {/* Lista de Requisitos Dinâmicos */}
                    <div className="requisitos-box">
                        <p>Requisitos para Cadastro:</p>
                        <ul>
                            <RequisitoItem label="Nome completo preenchido" valido={requisitos.nomePreenchido} />
                            <RequisitoItem label="Posição selecionada" valido={requisitos.posicaoSelecionada} />
                            <RequisitoItem label="Idade válida (mín. 16)" valido={requisitos.idadeValida} />
                        </ul>
                    </div>

                    {erroApi && <p className="error-message">{erroApi}</p>}

                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={!isFormularioValido}>
                            Salvar Atleta
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};