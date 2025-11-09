package dev.com.protactic.aplicacao.principal.clube;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Clube.
 */
public interface ClubeRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todos os clubes.
     * @return Lista de resumos de clubes.
     */
    List<ClubeResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de clubes
     * de uma competição específica.
     * @param competicaoId O ID da competição.
     * @return Lista de resumos de clubes.
     */
    List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId);
}