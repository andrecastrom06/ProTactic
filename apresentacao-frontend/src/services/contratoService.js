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

export const renovarContrato = async (contratoId, dadosRenovacao) => {
    try {
        const response = await fetch(`${API_BASE_URL}/contrato/renovar/${contratoId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dadosRenovacao)
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(erroTexto || "Erro ao renovar contrato");
        }
        return await response.json();
    } catch (error) {
        console.error("Erro na renovação:", error);
        throw error;
    }
};

export const dispensarJogador = async (jogadorId, usuarioId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/contrato/dispensar/${jogadorId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'usuarioId': usuarioId // <--- O HEADER OBRIGATÓRIO PARA O PROXY
            }
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(erroTexto || "Erro ao dispensar jogador");
        }
        return true;
    } catch (error) {
        console.error("Erro na dispensa:", error);
        throw error;
    }
};