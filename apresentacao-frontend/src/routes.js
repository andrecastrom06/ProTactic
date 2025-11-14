import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { MainLayout } from './layouts/MainLayout';
import { ProtectedRoute } from './layouts/ProtectedRoute';

// --- (PÁGINAS IMPORTADAS) ---
import { LoginPage } from './pages/Login/LoginPage';
import { ContratosPage } from './pages/Propostas/ContratosPage';

// --- (INÍCIO DA MUDANÇA) ---
// 1. Importe a nova página de Atletas
// (Assumindo que você usou 'export default' como sugeri)
import AtletasPage from './pages/Atletas/AtletasPage';
// --- (FIM DA MUDANÇA) ---


export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />

                {/* Rotas protegidas que usam o Layout Principal */}
                <Route element={<ProtectedRoute />}>
                    <Route element={<MainLayout />}>
                        
                        {/* Rotas principais */}
                        <Route path="/" element={<ContratosPage />} />
                        <Route path="/contratos" element={<ContratosPage />} />

                        {/* --- (INÍCIO DA MUDANÇA) --- */}
                        {/* 2. Substitua o <div> pelo componente da página */}
                        <Route path="/atletas" element={<AtletasPage />} />
                        {/* --- (FIM DA MUDANÇA) --- */}

                        {/* Placeholders para as outras páginas */}
                        <Route path="/dashboard" element={<div style={{padding: '20px'}}>Página de Dashboard</div>} />
                        <Route path="/competicoes" element={<div style={{padding: '20px'}}>Página de Competições</div>} />
                        <Route path="/treinos" element={<div style={{padding: '20px'}}>Página de Treinos</div>} />
                        <Route path="/jogos" element={<div style={{padding: '20px'}}>Página de Jogos</div>} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
};