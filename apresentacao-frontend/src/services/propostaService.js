import { API_BASE_URL } from '../config/apiConfig';

/**
 * CORRIGIDO: Busca apenas propostas ONDE o clube logado é o RECEPTOR.
 * Chama: GET /backend/proposta/pesquisa-por-receptor/{clubeId}
 */
export const buscarPropostasRecebidas = async (clubeId) => {
    if (!clubeId) return []; // Não faz nada se o ID do clube ainda não carregou

    try {
        const response = await fetch(`${API_BASE_URL}/proposta/pesquisa-por-receptor/${clubeId}`);
        
        if (!response.ok) {
            throw new Error(`Erro ao buscar propostas: ${response.statusText}`);
        }
        
        const dados = await response.json();
        return dados;

    } catch (error) {
        console.error("Falha na chamada da API (propostas):", error);
        throw error;
    }
};

/**
 * Aceita uma proposta de contratação.
 * Chama: POST /backend/proposta/aceitar/{propostaId}
 */
export const aceitarProposta = async (propostaId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/aceitar/${propostaId}`, {
            method: 'POST',
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao aceitar proposta: ${erroTexto}`);
        }
        
        return true; 

    } catch (error) {
        console.error("Falha na chamada da API (aceitar):", error);
        throw error;
    }
};

/**
 * Recusa uma proposta de contratação.
 * Chama: POST /backend/proposta/recusar/{propostaId}
 */
export const recusarProposta = async (propostaId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/recusar/${propostaId}`, {
            method: 'POST',
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao recusar proposta: ${erroTexto}`);
        }
        
        return true;

    } catch (error) {
        console.error("Falha na chamada da API (recusar):", error);
        throw error;
    }
};

/**
 * Cria uma nova proposta.
 * Chama: POST /backend/proposta/criar
 */
export const criarProposta = async (formulario) => {
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/criar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formulario),
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao criar proposta: ${erroTexto}`);
        }
        
        return true; // Sucesso

    } catch (error) {
        console.error("Falha na API (criar proposta):", error);
        throw error;
    }
};