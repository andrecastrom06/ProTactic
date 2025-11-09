package dev.com.protactic.aplicacao.principal.lesao;

import java.util.List;
import java.util.Optional; // Importar Optional

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Lesao.
 */
public interface LesaoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as lesões registradas.
     * @return Lista de resumos de lesões.
     */
    List<LesaoResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de todas as lesões
     * de um jogador específico.
     * @param jogadorId O ID do jogador.
     * @return Lista de resumos de lesões.
     */
    List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId);

    /**
     * Pesquisa e retorna o resumo da lesão ATIVA
     * de um jogador específico, se houver.
     * @param jogadorId O ID do jogador.
     * @return Um Optional contendo o resumo da lesão ativa, ou vazio.
     */
    Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId);
}