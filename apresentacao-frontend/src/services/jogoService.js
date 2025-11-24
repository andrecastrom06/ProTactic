import { API_BASE_URL } from '../config/apiConfig';


export const buscarJogosDoClube = async (clubeId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/partida/pesquisa`);
        if (!response.ok) throw new Error('Erro ao buscar jogos.');
        const todasPartidas = await response.json();
        
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

export const buscarEscalacaoPorPartida = async (partidaId, clubeId) => {
    try {
        let url = `${API_BASE_URL}/formacao/buscar-por-partida/${partidaId}`;
        if (clubeId) {
            url += `?clubeId=${clubeId}`;
        }

        const response = await fetch(url);
        
        if (response.status === 204) return null;
        if (!response.ok) throw new Error('Erro ao buscar escalação.');
        
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};


export const buscarEscalacaoDaPartida = async (partidaId, clubeId) => {
    try {
        let url = `${API_BASE_URL}/formacao/buscar-por-partida/${partidaId}`;
        if(clubeId) {
             url += `?clubeId=${clubeId}`;
        }
        
        const response = await fetch(url);
        
        if (response.status === 204) return null; 
        if (!response.ok) return null; 
        
        return await response.json(); 
    } catch (error) {
        console.error("Erro ao buscar escalação:", error);
        return null;
    }
};