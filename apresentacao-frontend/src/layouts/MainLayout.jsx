import React from 'react';
// Outlet é o placeholder do react-router
// onde as tuas páginas (ex: ListaPropostasPage) serão renderizadas
import { Outlet } from 'react-router-dom';

import { Sidebar } from '../components/Sidebar/Sidebar';
import './MainLayout.css';

export const MainLayout = () => {
    return (
        <div className="main-layout">
            {/* A Sidebar fica fixa à esquerda */}
            <Sidebar />
            
            {/* O conteúdo principal fica à direita */}
            <main className="main-content">
                {/* O Outlet renderiza a rota "filha" (a página atual) */}
                <Outlet />
            </main>
        </div>
    );
};