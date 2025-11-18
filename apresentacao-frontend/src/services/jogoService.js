import { API_BASE_URL } from '../config/apiConfig';

// --- PARTIDAS ---

export const buscarJogosDoClube = async (clubeId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/partida/pesquisa`);
        if (!response.ok) throw new Error('Erro ao buscar jogos.');
        const todasPartidas = await response.json();
        
        // Filtra localmente as partidas do clube
        return todasPartidas.filter(p => 
            p.clubeCasaId === clubeId || p.clubeVisitanteId === clubeId
        );
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const criarNovaPartida = async (dadosPartida) => {
    try {
        const response = await fetch(`${API_BASE_URL}/partida/criar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dadosPartida)
        });

        if (!response.ok) throw new Error('Erro ao criar partida.');
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

// --- ESCALAÇÃO / FORMAÇÃO ---

export const salvarEscalacao = async (formulario) => {
    try {
        const response = await fetch(`${API_BASE_URL}/formacao/salvar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formulario)
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || 'Erro ao salvar escalação.');
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const buscarEscalacaoPorPartida = async (partidaId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/formacao/buscar-por-partida/${partidaId}`);
        
        // Se não houver conteúdo (204), retorna null sem erro
        if (response.status === 204) return null;
        
        if (!response.ok) throw new Error('Erro ao buscar escalação.');
        
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};