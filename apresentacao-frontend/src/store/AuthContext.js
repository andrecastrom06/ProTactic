import React, { createContext, useState, useContext } from 'react';
import { autenticarUsuario } from '../services/loginService'; // Vamos criar este serviço

// 1. Cria o "Contexto" (a caixa de memória global)
const AuthContext = createContext(null);


export const AuthProvider = ({ children }) => {
    const [usuario, setUsuario] = useState(null);
    const [clubeIdLogado, setClubeIdLogado] = useState(null); 
    // --- (FIM DA MUDANÇA) ---

    const login = async (login, senha) => {
        try {
            const dadosDoUsuario = await autenticarUsuario(login, senha);
            setUsuario(dadosDoUsuario);
            // --- (INÍCIO DA MUDANÇA) ---
            setClubeIdLogado(dadosDoUsuario.clubeId); // Guarda o ID do clube
            // --- (FIM DA MUDANÇA) ---
        } catch (error) {
            console.error("Erro no login:", error);
            throw new Error("Login ou senha incorretos.");
        }
    };

    const logout = () => {
        setUsuario(null);
        setClubeIdLogado(null); // Limpa o ID do clube
    };

    const valor = {
        usuario,
        isAutenticado: !!usuario,
        clubeIdLogado, // <-- Expõe o ID do clube para a aplicação
        login,
        logout
    };

    return (
        <AuthContext.Provider value={valor}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};