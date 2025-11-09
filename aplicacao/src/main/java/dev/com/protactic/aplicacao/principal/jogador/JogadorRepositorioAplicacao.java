package dev.com.protactic.aplicacao.principal.jogador;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Jogador.
 */
public interface JogadorRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todos os jogadores.
     * @return Lista de resumos de jogadores.
     */
    List<JogadorResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de jogadores
     * de um clube específico.
     * @param clubeId O ID do clube.
     * @return Lista de resumos de jogadores.
     */
    List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId);
}