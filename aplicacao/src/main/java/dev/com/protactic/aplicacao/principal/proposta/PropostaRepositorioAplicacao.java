package dev.com.protactic.aplicacao.principal.proposta;

import java.util.List;

/**
 * Contrato de repositório para consultas (Queries)
 * da camada de Aplicação relacionadas a Proposta.
 */
public interface PropostaRepositorioAplicacao {
    
    /**
     * Pesquisa e retorna uma lista simples de todas as propostas.
     * @return Lista de resumos de propostas.
     */
    List<PropostaResumo> pesquisarResumos();

    /**
     * Pesquisa e retorna uma lista de resumos de propostas
     * enviadas por um clube específico (Propositor).
     * @param propositorId O ID do clube propositor.
     * @return Lista de resumos de propostas.
     */
    List<PropostaResumo> pesquisarResumosPorPropositor(Integer propositorId);

    /**
     * Pesquisa e retorna uma lista de resumos de propostas
     * recebidas por um clube específico (Receptor).
     * @param receptorId O ID do clube receptor.
     * @return Lista de resumos de propostas.
     */
    List<PropostaResumo> pesquisarResumosPorReceptor(Integer receptorId);

    /**
     * Pesquisa e retorna uma lista de resumos de propostas
     * relacionadas a um jogador específico.
     * @param jogadorId O ID do jogador.
     * @return Lista de resumos de propostas.
     */
    List<PropostaResumo> pesquisarResumosPorJogador(Integer jogadorId);
}