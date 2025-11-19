import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';


import { MainLayout } from './layouts/MainLayout';
import { ProtectedRoute } from './layouts/ProtectedRoute';
//Paginas importadas
import { LoginPage } from './pages/Login/LoginPage';
import { ContratosPage } from './pages/Propostas/ContratosPage';
import AtletasPage from './pages/Atletas/AtletasPage';
import { GestaoJogoPage } from './pages/GestaoJogo/GestaoJogoPage';
import { CompeticoesPage } from './pages/Competicoes/CompeticoesPage'; 
import { TreinosPage } from './pages/Treinos/TreinosPage';

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

                        {/* 2. Adicione a rota de competições */}
                        <Route path="/competicoes" element={<CompeticoesPage />} />

                        <Route path="/dashboard" element={<div style={{padding: '20px'}}>Página de Dashboard</div>} />
                        <Route path="/treinos" element={<TreinosPage />} />
                        <Route path="/jogos" element={<GestaoJogoPage />} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
};