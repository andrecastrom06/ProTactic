import { API_BASE_URL } from '../config/apiConfig';

/**
 * Registra uma nova lesão e, em seguida, cadastra o plano de recuperação.
 * Chama as duas APIs do RegistroDeLesaoControlador.
 */
export const registrarNovaLesao = async (jogadorId, grau, tipoLesao, previsaoRetorno) => {
    
    // --- ETAPA 1: POST /backend/lesao/registrar/{jogadorId} ---
    // (Envia apenas o 'grau')
    try {
        const registrarResponse = await fetch(`${API_BASE_URL}/lesao/registrar/${jogadorId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ grau: parseInt(grau, 10) })
        });

        if (!registrarResponse.ok) {
            const erroTexto = await registrarResponse.text();
            throw new Error(`Erro ao registrar lesão (Etapa 1): ${erroTexto}`);
        }
    } catch (error) {
        console.error("Falha na API (Registrar Lesão):", error);
        throw error;
    }

    // --- ETAPA 2: POST /backend/lesao/cadastrar-plano/{jogadorId} ---
    // (Envia 'tempo' e 'plano')
    try {
        // O backend espera 'tempo' como String, não 'date'. 
        // Vamos enviar a data como string por enquanto.
        const planoResponse = await fetch(`${API_BASE_URL}/lesao/cadastrar-plano/${jogadorId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tempo: previsaoRetorno, plano: tipoLesao })
        });

        if (!planoResponse.ok) {
            const erroTexto = await planoResponse.text();
            // Nota: Se falhar aqui, a lesão foi criada (Etapa 1) mas sem o plano.
            throw new Error(`Erro ao cadastrar plano (Etapa 2): ${erroTexto}`);
        }
        
        return true; // Sucesso nas duas etapas

    } catch (error) {
        console.error("Falha na API (Cadastrar Plano):", error);
        throw error;
    }
};

/**
 * Encerra a lesão ativa de um jogador.
 * Chama: POST /backend/lesao/encerrar/{jogadorId}
 */
export const encerrarLesao = async (jogadorId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/lesao/encerrar/${jogadorId}`, {
            method: 'POST',
        });

        if (!response.ok) {
            const erroTexto = await response.text();
            throw new Error(`Erro ao encerrar lesão: ${erroTexto}`);
        }
        
        return true; 

    } catch (error) {
        console.error("Falha na chamada da API (encerrar):", error);
        throw error;
    }
};