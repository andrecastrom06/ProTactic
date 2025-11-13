import React from 'react';
import { useAuth } from '../store/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';

/**
 * Este componente "guarda" as rotas.
 * Se o utilizador estiver autenticado, mostra o <Outlet /> (a página).
 * Se não, redireciona para a página de login.
 */
export const ProtectedRoute = () => {
    const { isAutenticado } = useAuth();

    if (!isAutenticado) {
        // Redireciona para a página de login
        return <Navigate to="/login" replace />;
    }

    // Mostra a página que o utilizador tentou aceder (ex: /dashboard)
    return <Outlet />;
};