import { API_BASE_URL } from '../config/apiConfig';

/**
 * Busca todos os contratos vigentes do clube logado.
 * Chama: GET /backend/contrato/pesquisa-por-clube/{clubeId}
 */
export const buscarContratosVigentes = async (clubeId) => {
    if (!clubeId) return []; // Não faz nada se o ID do clube ainda não carregou

    try {
        // Usa o ID dinâmico
        const response = await fetch(`${API_BASE_URL}/contrato/pesquisa-por-clube/${clubeId}`);
        
        if (!response.ok) {
            throw new Error(`Erro ao buscar contratos: ${response.statusText}`);
        }
        return await response.json();

    } catch (error) {
        console.error("Falha na API de Contratos:", error);
        throw error;
    }
};

// TODO: Adicionar aqui as funções para Renovar e Dispensar
// export const renovarContrato = async (contratoId, formulario) => { ... }
// export const dispensarJogador = async (jogadorId) => { ... }