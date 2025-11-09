package dev.com.protactic.aplicacao.principal.escalacao;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Escalacao.
 */
public interface EscalacaoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista de resumos de escalação
     * para uma data de jogo específica.
     * @param jogoData A data/identificador do jogo.
     * @return Lista de resumos de escalação.
     */
    List<EscalacaoResumo> pesquisarResumosPorData(String jogoData);
}