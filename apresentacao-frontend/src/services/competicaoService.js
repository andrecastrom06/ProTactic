import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todas as competições cadastradas.
 * Chama: GET /backend/competicao/pesquisa
 */
export const buscarTodasCompeticoes = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/competicao/pesquisa`);
        if (!response.ok) {
            throw new Error(`Erro ao buscar competições: ${response.statusText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Falha na API de Competições:", error);
        throw error;
    }
};