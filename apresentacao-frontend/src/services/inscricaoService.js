import { API_BASE_URL } from '../config/apiConfig';

/**
 * Registra um atleta (pelo nome) em uma competição (pelo nome).
 * Chama: POST /backend/inscricao/salvar
 */
export const registrarInscricao = async (atletaNome, competicaoNome) => {
    try {
        const response = await fetch(`${API_BASE_URL}/inscricao/salvar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ atleta: atletaNome, competicao: competicaoNome })
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao registrar inscrição: ${erroTexto}`);
        }
        
        // O backend retorna o objeto InscricaoAtleta com o status
        return await response.json(); 

    } catch (error) {
        console.error("Falha na API (Registrar Inscrição):", error);
        throw error;
    }
};

/**
 * Busca todas as inscrições para uma competição específica.
 * Chama: GET /backend/inscricao/competicao/{nome}
 */
export const buscarInscricoesPorCompeticao = async (competicaoNome) => {
    try {
        const response = await fetch(`${API_BASE_URL}/inscricao/competicao/${encodeURIComponent(competicaoNome)}`);
        if (!response.ok) {
            throw new Error(`Erro ao buscar inscrições: ${response.statusText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Falha na API de Inscrições:", error);
        throw error;
    }
};