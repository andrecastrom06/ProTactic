import { NavLink } from 'react-router-dom';
import { useAuth } from '../../store/AuthContext';
import './Sidebar.css';
import { LuHouse, LuClipboardList, LuX } from "react-icons/lu"; // Importe o LuX
import { FiUsers, FiFileText } from "react-icons/fi";
import { GoTrophy } from "react-icons/go";
import { IoCalendarClearOutline } from "react-icons/io5";

export const Sidebar = ({ isOpen, onClose }) => {
    const { usuario, logout } = useAuth(); 

    return (
        <>
            <div 
                className={`sidebar-overlay ${isOpen ? 'open' : ''}`}
                onClick={onClose} 
            />
            
            <aside className={`sidebar ${isOpen ? 'open' : ''}`}>
                <div className="sidebar-header">
                    <div className="sidebar-header-top">
                        <h1>ProTactic</h1>
                        {/* 4. Botão de fechar (X) para mobile */}
                        <button className="sidebar-close-btn" onClick={onClose}>
                            <LuX size={24} />
                        </button>
                    </div>
                    <p>Gestão Integrada</p>
                </div>
                
                <nav className="sidebar-nav">
                    <ul>
                        {/* 5. (Opcional) Fechar ao clicar no link */}
                        <li><NavLink to="/dashboard" onClick={onClose}><LuHouse /> Dashboard</NavLink></li>
                        <li><NavLink to="/atletas" onClick={onClose}><FiUsers /> Atletas</NavLink></li>
                        <li><NavLink to="/competicoes" onClick={onClose}><GoTrophy /> Competições</NavLink></li>
                        <li><NavLink to="/treinos" onClick={onClose}><IoCalendarClearOutline /> Treinos</NavLink></li>
                        <li><NavLink to="/jogos" onClick={onClose}><LuClipboardList /> Jogos</NavLink></li>
                        <li><NavLink to="/contratos" onClick={onClose}><FiFileText /> Contratos</NavLink></li>
                    </ul>
                </nav>
                
                <div className="sidebar-footer">
                    {usuario ? (
                        <>
                            <div className="user-avatar">
                                {usuario.nome ? usuario.nome.match(/\b(\w)/g).join('') : 'U'}
                            </div>
                            <div className="user-info">
                                <p className="user-name">{usuario.nome}</p>
                                <p className="user-role">{usuario.funcao || 'Usuário'}</p>
                            </div>
                            <button onClick={logout} className="logout-btn">Sair</button>
                        </>
                    ) : (
                        <p>Não autenticado.</p>
                    )}
                </div>
            </aside>
        </>
    );
};