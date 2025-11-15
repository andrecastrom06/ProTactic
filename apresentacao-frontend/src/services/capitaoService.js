import { API_BASE_URL } from '../config/apiConfig';

/**
 * Define um jogador como o novo capitão do time.
 * Chama: POST /backend/capitao/definir/{jogadorId}
 */
export const definirCapitao = async (jogadorId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/capitao/definir/${jogadorId}`, {
            method: 'POST',
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao definir capitão: ${erroTexto}`);
        }
        
        return true; 

    } catch (error) {
        console.error("Falha na chamada da API (Definir Capitão):", error);
        throw error;
    }
};