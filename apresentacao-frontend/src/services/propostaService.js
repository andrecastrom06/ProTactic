import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todas as propostas do backend.
 * Chama: GET /backend/proposta/pesquisa
 */
export const buscarTodasPropostas = async () => {
    // ... (função existente, não mexe)
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/pesquisa`);
        
        if (!response.ok) {
            throw new Error(`Erro ao buscar propostas: ${response.statusText}`);
        }
        
        const dados = await response.json();
        return dados;

    } catch (error) {
        console.error("Falha na chamada da API:", error);
        throw error;
    }
};

// --- (INÍCIO DA NOVA FUNCIONALIDADE) ---

/**
 * Aceita uma proposta de contratação.
 * Chama: POST /backend/proposta/aceitar/{propostaId}
 */
export const aceitarProposta = async (propostaId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/aceitar/${propostaId}`, {
            method: 'POST', // Define o método como POST
        });

        if (!response.ok) {
            // Tenta ler a mensagem de erro do backend
            const erroTexto = await response.text();
            throw new Error(`Erro ao aceitar proposta: ${erroTexto}`);
        }
        
        // Se deu 200 OK, não precisa retornar nada
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
            method: 'POST', // Define o método como POST
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
export const criarProposta = async (formulario) => {
    try {
        const response = await fetch(`${API_BASE_URL}/proposta/criar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formulario), // Envia o formulário como JSON
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
// --- (FIM DA NOVA FUNCIONALIDADE) ---