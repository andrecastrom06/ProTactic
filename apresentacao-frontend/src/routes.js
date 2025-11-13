import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

// 1. Importa a tua primeira página
import { ListaPropostasPage } from './pages/Propostas/ListaPropostasPage';

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                {/* 2. Define a rota inicial ("/") para mostrar a página de propostas */}
                <Route path="/" element={<ListaPropostasPage />} />
                
                {/* (Aqui adicionaremos mais rotas no futuro) */}
            </Routes>
        </BrowserRouter>
    );
};