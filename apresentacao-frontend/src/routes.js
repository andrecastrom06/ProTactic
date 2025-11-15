import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { MainLayout } from './layouts/MainLayout';
import { ProtectedRoute } from './layouts/ProtectedRoute';

// --- (PÁGINAS IMPORTADAS) ---
import { LoginPage } from './pages/Login/LoginPage';
import { ContratosPage } from './pages/Propostas/ContratosPage';
import AtletasPage from './pages/Atletas/AtletasPage';
import { GestaoJogoPage } from './pages/GestaoJogo/GestaoJogoPage';

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

                        <Route path="/atletas" element={<AtletasPage />} />

                        <Route path="/dashboard" element={<div style={{padding: '20px'}}>Página de Dashboard</div>} />
                        <Route path="/competicoes" element={<div style={{padding: '20px'}}>Página de Competições</div>} />
                        <Route path="/treinos" element={<div style={{padding: '20px'}}>Página de Treinos</div>} />
                        <Route path="/jogos" element={<GestaoJogoPage />} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
};