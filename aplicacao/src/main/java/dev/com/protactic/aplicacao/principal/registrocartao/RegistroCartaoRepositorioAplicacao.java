package dev.com.protactic.aplicacao.principal.registrocartao;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a RegistroCartao.
 */
public interface RegistroCartaoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todos os registros de cartões.
     * @return Lista de resumos de registros.
     */
    List<RegistroCartaoResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de registros de cartões
     * de um atleta específico.
     * @param atleta O nome do atleta.
     * @return Lista de resumos de registros.
     */
    List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta);

    /**
     * Pesquisa e retorna uma lista de resumos de registros de cartões
     * de um tipo específico (ex: "AMARELO").
     * @param tipo O tipo do cartão.
     * @return Lista de resumos de registros.
     */
    List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo);
}