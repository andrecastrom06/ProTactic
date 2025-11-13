import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todos os clubes do backend.
 * Chama: GET /backend/clube/pesquisa
 */
export const buscarTodosClubes = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/clube/pesquisa`);
        if (!response.ok) {
            throw new Error(`Erro ao buscar clubes: ${response.statusText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Falha na API de Clubes:", error);
        throw error;
    }
};