import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { MainLayout } from './layouts/MainLayout';
import { ProtectedRoute } from './layouts/ProtectedRoute';

// --- (INÍCIO DA MUDANÇA) ---
// 1. Importa a página com o novo nome
import { ContratosPage } from './pages/Propostas/ContratosPage';
// --- (FIM DA MUDANÇA) ---

import { LoginPage } from './pages/Login/LoginPage';

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />

                <Route element={<ProtectedRoute />}>
                    <Route element={<MainLayout />}>
                        
                        {/* --- (INÍCIO DA MUDANÇA) --- */}
                        {/* 2. Atualiza as rotas para a página de Contratos */}
                        <Route path="/" element={<ContratosPage />} />
                        <Route path="/contratos" element={<ContratosPage />} />
                        {/* --- (FIM DA MUDANÇA) --- */}

                        <Route path="/dashboard" element={<div style={{padding: '20px'}}>Página de Dashboard</div>} />
                        <Route path="/atletas" element={<div style={{padding: '20px'}}>Página de Atletas</div>} />
                        <Route path="/competicoes" element={<div style={{padding: '20px'}}>Página de Competições</div>} />
                        <Route path="/treinos" element={<div style={{padding: '20px'}}>Página de Treinos</div>} />
                        <Route path="/jogos" element={<div style={{padding: '20px'}}>Página de Jogos</div>} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
};