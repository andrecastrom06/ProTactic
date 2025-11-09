package dev.com.protactic.aplicacao.principal.contrato;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Contrato.
 */
public interface ContratoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todos os contratos.
     * @return Lista de resumos de contratos.
     */
    List<ContratoResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de contratos
     * de um clube específico.
     * @param clubeId O ID do clube.
     * @return Lista de resumos de contratos.
     */
    List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId);
}