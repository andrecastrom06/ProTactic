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
 * Chama: POST /backend/premiacao/salvar
 * Payload: { jogadorId, nome, dataPremiacao }
 */
export const salvarPremiacao = async (formulario) => {
    try {
        const response = await fetch(`${API_BASE_URL}/premiacao/salvar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formulario)
        });

        if (!response.ok) {
            // O backend pode não retornar JSON no caso de void/erro, então lemos texto
            const text = await response.text(); 
            throw new Error(text || 'Erro ao salvar premiação.');
        }
        
        return true;
    } catch (error) {
        console.error(error);
        throw error;
    }
};