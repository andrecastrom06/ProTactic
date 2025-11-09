package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a SessaoTreino.
 */
public interface SessaoTreinoRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as sessões de treino.
     * @return Lista de resumos de sessões.
     */
    List<SessaoTreinoResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de sessões de treino
     * associadas a uma partida específica.
     * @param partidaId O ID da partida.
     * @return Lista de resumos de sessões.
     */
    List<SessaoTreinoResumo> pesquisarResumosPorPartida(Integer partidaId);

    /**
     * Pesquisa e retorna uma lista de resumos de sessões de treino
     * onde um jogador específico foi convocado.
     * @param jogadorId O ID do jogador.
     * @return Lista de resumos de sessões.
     */
    List<SessaoTreinoResumo> pesquisarResumosPorConvocado(Integer jogadorId);
}