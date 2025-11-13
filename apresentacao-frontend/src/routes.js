import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

// Layouts e Rotas
import { MainLayout } from './layouts/MainLayout';
import { ProtectedRoute } from './layouts/ProtectedRoute'; // 1. Importa o "guarda"

// Páginas
import { LoginPage } from './pages/Login/LoginPage'; // 2. Importa a Página de Login
import { ListaPropostasPage } from './pages/Propostas/ListaPropostasPage';

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                
                {/* Rota 1: Página de Login (Pública) */}
                <Route path="/login" element={<LoginPage />} />

                {/* Rota 2: Rotas Protegidas (Privadas) */}
                {/* O <ProtectedRoute> verifica se o user está logado */}
                <Route element={<ProtectedRoute />}>
                    {/* Se estiver logado, mostra o <MainLayout> */}
                    <Route element={<MainLayout />}>
                        {/* E aqui vêm todas as tuas páginas internas */}
                        <Route path="/" element={<ListaPropostasPage />} />
                        <Route path="/contratos" element={<ListaPropostasPage />} />
                        <Route path="/dashboard" element={<div style={{padding: '20px'}}>Página de Dashboard</div>} />
                        <Route path="/atletas" element={<div style={{padding: '20px'}}>Página de Atletas</div>} />
                        {/* ... etc ... */}
                    </Route>
                </Route>

            </Routes>
        </BrowserRouter>
    );
};