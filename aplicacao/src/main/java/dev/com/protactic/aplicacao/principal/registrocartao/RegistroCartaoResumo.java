package dev.com.protactic.aplicacao.principal.registrocartao;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade RegistroCartao.
 * Contém apenas os dados necessários para listagens.
 */
public interface RegistroCartaoResumo {
    
    Integer getId();
    String getAtleta();
    String getTipo();

}