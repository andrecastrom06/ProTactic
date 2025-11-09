package dev.com.protactic.aplicacao.principal.inscricaoatleta;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a InscricaoAtleta.
 */
public interface InscricaoAtletaRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as inscrições.
     * @return Lista de resumos de inscrições.
     */
    List<InscricaoAtletaResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de inscrições
     * de um atleta específico.
     * @param atleta O nome do atleta.
     * @return Lista de resumos de inscrições.
     */
    List<InscricaoAtletaResumo> pesquisarResumosPorAtleta(String atleta);

    /**
     * Pesquisa e retorna uma lista de resumos de inscrições
     * de uma competição específica.
     * @param competicao O nome da competição.
     * @return Lista de resumos de inscrições.
     */
    List<InscricaoAtletaResumo> pesquisarResumosPorCompeticao(String competicao);
}