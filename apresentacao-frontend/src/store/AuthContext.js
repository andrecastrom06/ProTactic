import React, { createContext, useState, useContext } from 'react';
import { autenticarUsuario } from '../services/loginService'; // Vamos criar este serviço

// 1. Cria o "Contexto" (a caixa de memória global)
const AuthContext = createContext(null);

/**
 * O "Provedor" do contexto. É um componente que envolve a tua aplicação
 * e "providencia" os dados de autenticação a todos os seus filhos.
 */
export const AuthProvider = ({ children }) => {
    // Guarda o objeto do usuário (ex: { id: 1, nome: "Carlos Técnico" })
    const [usuario, setUsuario] = useState(null);

    // Função que a página de Login vai chamar
    const login = async (login, senha) => {
        try {
            // Chama a API de login que criámos no backend
            const dadosDoUsuario = await autenticarUsuario(login, senha);
            setUsuario(dadosDoUsuario); // Salva o usuário no estado
        } catch (error) {
            console.error("Erro no login:", error);
            throw new Error("Login ou senha incorretos.");
        }
    };

    // Função para o botão "Sair"
    const logout = () => {
        setUsuario(null); // Limpa o usuário
    };

    // Os "valores" que queremos partilhar com a aplicação
    const valor = {
        usuario,    // O objeto do usuário atual
        isAutenticado: !!usuario, // Um booleano (true/false)
        login,      // A função de login
        logout      // A função de logout
    };

    // Retorna o "Provedor" que envolve a aplicação
    return (
        <AuthContext.Provider value={valor}>
            {children}
        </AuthContext.Provider>
    );
};

/**
 * Um "Hook" customizado para facilitar o acesso aos dados.
 * Em vez de usarmos "useContext(AuthContext)" em todo o lado,
 * podemos simplesmente usar "useAuth()".
 */
export const useAuth = () => {
    return useContext(AuthContext);
};