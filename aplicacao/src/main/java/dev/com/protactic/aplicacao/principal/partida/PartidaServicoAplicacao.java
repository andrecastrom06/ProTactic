package dev.com.protactic.aplicacao.principal.partida;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Partidas.
 */
@Service // 1. Define como um "Bean" do Spring
public class PartidaServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final PartidaRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public PartidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as partidas (resumido)
     */
    public List<PartidaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar partidas por clube da casa (resumido)
     */
    public List<PartidaResumo> pesquisarResumosPorClubeCasa(Integer clubeCasaId) {
        return repositorio.pesquisarResumosPorClubeCasa(clubeCasaId);
    }

    /**
     * Caso de Uso: Listar partidas por clube visitante (resumido)
     */
    public List<PartidaResumo> pesquisarResumosPorClubeVisitante(Integer clubeVisitanteId) {
        return repositorio.pesquisarResumosPorClubeVisitante(clubeVisitanteId);
    }
}