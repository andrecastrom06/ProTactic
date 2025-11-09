package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Premiacao.
 */
public interface PremiacaoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as premiações.
     * @return Lista de resumos de premiações.
     */
    List<PremiacaoResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de premiações
     * de um jogador específico.
     * @param jogadorId O ID do jogador.
     * @return Lista de resumos de premiações.
     */
    List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId);

    /**
     * Pesquisa e retorna uma lista de resumos de premiações
     * por nome (ex: "Melhor em Campo").
     * @param nome O nome da premiação.
     * @return Lista de resumos de premiações.
     */
    List<PremiacaoResumo> pesquisarResumosPorNome(String nome);
}