package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Sessoes de Treino.
 */
@Service // 1. Define como um "Bean" do Spring
public class SessaoTreinoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final SessaoTreinoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public SessaoTreinoServicoAplicacao(SessaoTreinoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as sessões (resumido)
     */
    public List<SessaoTreinoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar sessões por partida (resumido)
     */
    public List<SessaoTreinoResumo> pesquisarResumosPorPartida(Integer partidaId) {
        return repositorio.pesquisarResumosPorPartida(partidaId);
    }

    /**
     * Caso de Uso: Listar sessões por jogador convocado (resumido)
     */
    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(Integer jogadorId) {
        return repositorio.pesquisarResumosPorConvocado(jogadorId);
    }
}