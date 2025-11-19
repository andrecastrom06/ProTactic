import { API_BASE_URL } from '../config/apiConfig';

// --- SESSÕES TÁTICAS ---

export const buscarTodasSessoesTaticas = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/sessao-treino/pesquisa`); // Ajuste conforme seu endpoint real
        if (!response.ok) return []; 
        return await response.json();
    } catch (error) {
        console.error("Erro buscar táticos:", error);
        return [];
    }
};

export const criarSessaoTatica = async (payload) => {
    const response = await fetch(`${API_BASE_URL}/sessao-treino/criar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) {
        const texto = await response.text();
        throw new Error(texto || 'Erro ao criar sessão tática.');
    }
    return true;
};

// --- TREINOS FÍSICOS ---

export const buscarTreinosFisicosPorJogador = async (jogadorId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/treino-fisico/por-jogador/${jogadorId}`);
        if (!response.ok) return [];
        return await response.json();
    } catch (error) {
        console.error("Erro buscar físicos:", error);
        return [];
    }
};

export const salvarTreinoFisico = async (jogadorId, treinoData) => {
    const response = await fetch(`${API_BASE_URL}/treino-fisico/salvar/${jogadorId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(treinoData)
    });
    if (!response.ok) {
        const erro = await response.text();
        throw new Error(erro || "Erro ao salvar treino físico.");
    }
    return await response.json();
};

export const atualizarStatusTreinoFisico = async (treinoId, novoStatus) => {
    try {
        const response = await fetch(`${API_BASE_URL}/treino-fisico/atualizar-status/${treinoId}`, {
            method: 'PATCH', // O Controlador usa @PatchMapping
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: novoStatus })
        });

        if (!response.ok) {
            throw new Error("Erro ao atualizar status.");
        }
        return true;
    } catch (error) {
        console.error(error);
        throw error;
    }
};