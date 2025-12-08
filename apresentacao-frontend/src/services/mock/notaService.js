import { API_BASE_URL } from '../config/apiConfig';

// Função existente (mantém ou atualiza se não tiveres)
export const atribuirNota = async (payload) => {
    try {
        const response = await fetch(`${API_BASE_URL}/nota/atribuir`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const erro = await response.text();
            throw new Error(erro);
        }
        return true;
    } catch (error) {
        console.error("Erro ao atribuir nota:", error);
        throw error;
    }
};

export const buscarNotasPorJogo = async (jogoId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/nota/pesquisa-por-jogo/${jogoId}`);
        if (!response.ok) {
            return [];
        }
        return await response.json();
    } catch (error) {
        console.error("Erro ao buscar notas:", error);
        return [];
    }
};