package dev.com.protactic.aplicacao.principal.escalacao;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade EscalacaoSimples.
 * Usado para listagens de consulta.
 */
public interface EscalacaoResumo {
    
    Integer getId();
    String getJogoData();
    String getNomeJogador();

}