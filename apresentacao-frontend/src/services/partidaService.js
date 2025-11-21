import { API_BASE_URL } from '../config/apiConfig';

export const partidaService = {

    registrarEstatisticas: async (listaEstatisticas) => {
        const response = await fetch(`${API_BASE_URL}/partida/registrar-estatisticas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(listaEstatisticas)
        });

        if (!response.ok) throw new Error('Erro ao salvar estatísticas.');
    }
};