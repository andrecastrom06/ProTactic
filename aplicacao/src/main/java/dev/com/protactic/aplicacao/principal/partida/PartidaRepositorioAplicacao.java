package dev.com.protactic.aplicacao.principal.partida;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Partida.
 */
public interface PartidaRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as partidas.
     * @return Lista de resumos de partidas.
     */
    List<PartidaResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de partidas
     * de um clube específico (como casa).
     * @param clubeCasaId O ID do clube da casa.
     * @return Lista de resumos de partidas.
     */
    List<PartidaResumo> pesquisarResumosPorClubeCasa(Integer clubeCasaId);

    /**
     * Pesquisa e retorna uma lista de resumos de partidas
     * de um clube específico (como visitante).
     * @param clubeVisitanteId O ID do clube visitante.
     * @return Lista de resumos de partidas.
     */
    List<PartidaResumo> pesquisarResumosPorClubeVisitante(Integer clubeVisitanteId);
}