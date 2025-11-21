import { API_BASE_URL } from '../config/apiConfig'; 

const ENDPOINT = `${API_BASE_URL}/registro-cartoes`; 

export const cartaoService = {
    listarTodos: async () => {
        const response = await fetch(`${ENDPOINT}/pesquisa`);
        if (!response.ok) throw new Error('Erro ao buscar cartões');
        return response.json();
    },

    listarPorAtleta: async (atletaId) => {
        const response = await fetch(`${ENDPOINT}/pesquisa-por-atleta/${atletaId}`);
        if (!response.ok) throw new Error('Erro ao buscar cartões do atleta');
        return response.json();
    },

    registrarCartao: async (atletaId, tipo) => {
        const response = await fetch(`${ENDPOINT}/registrar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                atleta: atletaId, 
                tipo: tipo 
            })
        });

        if (!response.ok) {
            const erro = await response.text();
            throw new Error(erro || 'Erro ao registrar cartão');
        }
    },

    limparCartoes: async (atletaId) => {
        const response = await fetch(`${ENDPOINT}/limpar/${atletaId}`, {
            method: 'POST'
        });
        if (!response.ok) throw new Error('Erro ao limpar cartões');
    }
};