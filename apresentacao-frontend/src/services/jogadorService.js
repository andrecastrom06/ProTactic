import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todos os jogadores do backend.
 * Chama: GET /backend/capitao/pesquisa-jogadores
 * (Nota: O endpoint está no CapitaoControlador, mas busca jogadores)
 */
export const buscarTodosJogadores = async () => {
    try {
        // ATENÇÃO: O endpoint para buscar jogadores está no CapitaoControlador
        const response = await fetch(`${API_BASE_URL}/capitao/pesquisa-jogadores`);
        if (!response.ok) {
            throw new Error(`Erro ao buscar jogadores: ${response.statusText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Falha na API de Jogadores:", error);
        throw error;
    }
};