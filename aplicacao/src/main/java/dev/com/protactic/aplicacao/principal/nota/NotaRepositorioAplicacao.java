package dev.com.protactic.aplicacao.principal.nota;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Nota.
 */
public interface NotaRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista de resumos de notas
     * de um jogo específico.
     * @param jogoId O ID/Data do jogo.
     * @return Lista de resumos de notas.
     */
    List<NotaResumo> pesquisarResumosPorJogo(String jogoId);

    /**
     * Pesquisa e retorna uma lista de resumos de notas
     * de um jogador específico.
     * @param jogadorId O ID do jogador.
     * @return Lista de resumos de notas.
     */
    List<NotaResumo> pesquisarResumosPorJogador(String jogadorId);
}