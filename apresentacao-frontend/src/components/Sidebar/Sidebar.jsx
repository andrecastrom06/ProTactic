import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../store/AuthContext'; // 1. Importa o hook
import './Sidebar.css';

export const Sidebar = () => {
    // 2. Acede aos dados de autenticação
    const { usuario, logout } = useAuth(); 

    return (
        <aside className="sidebar">
            {/* ... (O <sidebar-header> e <sidebar-nav> ficam iguais) ... */}
            <div className="sidebar-header">
                <h1>ProTactic</h1>
                <p>Gestão Integrada</p>
            </div>
            
            <nav className="sidebar-nav">
                <ul>
                    <li><NavLink to="/dashboard">Dashboard</NavLink></li>
                    <li><NavLink to="/atletas">Atletas</NavLink></li>
                    <li><NavLink to="/competicoes">Competições</NavLink></li>
                    <li><NavLink to="/treinos">Treinos</NavLink></li>
                    <li><NavLink to="/jogos">Jogos</NavLink></li>
                    <li><NavLink to="/contratos">Contratos</NavLink></li>
                </ul>
            </nav>
            
            {/* --- (INÍCIO DA MUDANÇA) --- */}
            {/* 3. Renderiza os dados do usuário (se ele existir) */}
            <div className="sidebar-footer">
                {usuario ? (
                    <>
                        <div className="user-avatar">
                            {usuario.nome ? usuario.nome.match(/\b(\w)/g).join('') : 'U'}
                        </div>
                        <div className="user-info">
                            <p className="user-name">{usuario.nome}</p>
                            
                            {/* --- (INÍCIO DA MUDANÇA) --- */}
                            {/* Agora lê a função dinâmica vinda da API */}
                            <p className="user-role">{usuario.funcao || 'Usuário'}</p>
                            {/* --- (FIM DA MUDANÇA) --- */}
                        </div>
                        <button onClick={logout} className="logout-btn">Sair</button>
                    </>
                ) : (
                    <p>Não autenticado.</p>
                )}
            </div>
        </aside>
    );
};