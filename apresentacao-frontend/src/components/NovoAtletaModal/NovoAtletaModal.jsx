import React, { useState, useMemo } from 'react';
import Swal from 'sweetalert2'; // Importação do SweetAlert2
import { useAuth } from '../../store/AuthContext';
import { cadastrarNovoAtleta } from '../../services/jogadorService';
import './NovoAtletaModal.css';

const POSICOES = ["Goleiro", "Lateral", "Zagueiro", "Meio-campo", "Atacante"];

export const NovoAtletaModal = ({ onClose, onSuccess }) => {
    const { clubeIdLogado } = useAuth();

    const [nome, setNome] = useState('');
    const [posicao, setPosicao] = useState('');
    const [idade, setIdade] = useState('');
    const [duracaoMeses, setDuracaoMeses] = useState('');
    const [salario, setSalario] = useState('');
    
    const [isSubmitting, setIsSubmitting] = useState(false);

    const requisitos = useMemo(() => {
        return {
            nomePreenchido: nome.trim().length > 2,
            posicaoSelecionada: POSICOES.includes(posicao),
            idadeValida: parseInt(idade, 10) > 15,
            contratoValido: parseInt(duracaoMeses, 10) > 0,
            salarioValido: parseFloat(salario) > 0,
        };
    }, [nome, posicao, idade, duracaoMeses, salario]);

    const isFormularioValido = Object.values(requisitos).every(Boolean);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        if (!isFormularioValido) {
            Swal.fire({
                title: 'Atenção',
                text: 'Por favor, preencha todos os requisitos corretamente.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            setIsSubmitting(false);
            return;
        }

        try {
            const formulario = {
                nomeCompleto: nome,
                posicao: posicao,
                idade: parseInt(idade, 10),
                duracaoMeses: parseInt(duracaoMeses, 10),
                salario: parseFloat(salario),
                situacaoContratual: "ATIVO",
                clubeId: clubeIdLogado
            };

            await cadastrarNovoAtleta(formulario);
            
            await Swal.fire({
                title: 'Sucesso!',
                text: 'Atleta contratado e adicionado ao elenco.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            onSuccess(); 
            
        } catch (error) {
            console.error("Erro ao cadastrar atleta:", error);
            const errorMessage = error.message || "Falha ao cadastrar atleta. Tente novamente.";

            Swal.fire({
                title: 'Erro',
                text: errorMessage,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        } finally {
            setIsSubmitting(false);
        }
    };

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
                
                <p className="modal-subtitle">Insira os dados do novo reforço do clube.</p>

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
                    </div>
                    
                    <div className="form-group-row">
                        <div className="form-group">
                            <label>Duração do Contrato (Meses) *</label>
                            <input 
                                type="number" 
                                value={duracaoMeses} 
                                onChange={(e) => setDuracaoMeses(e.target.value)} 
                                placeholder="Ex: 36"
                            />
                        </div>
                        <div className="form-group">
                            <label>Salário Mensal (R$) *</label>
                            <input 
                                type="number" 
                                value={salario} 
                                onChange={(e) => setSalario(e.target.value)} 
                                placeholder="Ex: 150000"
                            />
                        </div>
                    </div>

                    <div className="requisitos-box">
                        <p>Requisitos:</p>
                        <ul>
                            <RequisitoItem label="Nome preenchido" valido={requisitos.nomePreenchido} />
                            <RequisitoItem label="Posição selecionada" valido={requisitos.posicaoSelecionada} />
                            <RequisitoItem label="Duração válida (> 0)" valido={requisitos.contratoValido} />
                            <RequisitoItem label="Salário válido (> 0)" valido={requisitos.salarioValido} />
                        </ul>
                    </div>


                    <div className="modal-actions">
                        <button type="button" className="btn-cancel" onClick={onClose} disabled={isSubmitting}>Cancelar</button>
                        <button type="submit" className="btn-submit" disabled={!isFormularioValido || isSubmitting}>
                            {isSubmitting ? 'Contratando...' : 'Contratar'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};