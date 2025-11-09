package dev.com.protactic.aplicacao.principal.proposta;

import java.util.Date;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Proposta.
 * Contém apenas os dados necessários para listagens.
 */
public interface PropostaResumo {
    
    int getId();
    Integer getPropositorId();
    Integer getReceptorId();
    Integer getJogadorId();
    String getStatus();
    double getValor();
    Date getData();

}