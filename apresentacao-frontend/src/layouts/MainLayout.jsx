import React, { useState } from 'react'; // 1. Importe o useState
import { Outlet } from 'react-router-dom';
import { Sidebar } from '../components/Sidebar/Sidebar';
import { FiMenu } from 'react-icons/fi'; // 2. Importe o ícone do "hambúrguer"
import './MainLayout.css';

export const MainLayout = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    const closeSidebar = () => {
        setIsSidebarOpen(false);
    };

    return (
        <div className="main-layout">
            <Sidebar isOpen={isSidebarOpen} onClose={closeSidebar} />
            
            <header className="mobile-header">
                <button className="hamburger-btn" onClick={toggleSidebar}>
                    <FiMenu size={26} />
                </button>
                <h1 className="mobile-header-title">ProTactic</h1>
            </header>

            <main className="main-content">
                <Outlet />
            </main>
        </div>
    );
};