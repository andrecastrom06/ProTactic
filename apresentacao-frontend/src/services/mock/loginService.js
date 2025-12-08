import { API_BASE_URL } from '../config/apiConfig';

/**
 * Tenta autenticar o usuário no backend.
 * Chama: POST /backend/login
 */
export const autenticarUsuario = async (login, senha) => {
    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ login, senha }),
        });

        if (!response.ok) { // Erro 401 (Unauthorized) ou 500
            throw new Error('Credenciais inválidas');
        }
        
        return await response.json(); // Retorna o objeto Usuario
        
    } catch (error) {
        console.error("Falha na API de Login:", error);
        throw error;
    }
};