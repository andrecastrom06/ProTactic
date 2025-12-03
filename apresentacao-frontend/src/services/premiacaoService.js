import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todas as premiações cadastradas.
 * Chama: GET /backend/premiacao/todos
 */
export const buscarTodasPremiacoes = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/premiacao/todos`);
        if (!response.ok) {
            throw new Error('Erro ao buscar premiações.');
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

/**
 * Salva uma nova premiação.
 * O Backend calcula automaticamente o vencedor e o valor (Decorator).
 * Chama: POST /backend/premiacao/salvar
 * Payload: { nome, dataPremiacao }
 */
export const salvarPremiacao = async (nome, dataPremiacao) => {
    try {
        const payload = { nome, dataPremiacao }; 
        const response = await fetch(`${API_BASE_URL}/premiacao/salvar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const text = await response.text(); 
            throw new Error(text || 'Erro ao salvar premiação.');
        }
        
        // Tenta retornar JSON se houver, senão true
        try {
            return await response.json();
        } catch {
            return true;
        }
    } catch (error) {
        console.error(error);
        throw error;
    }
};