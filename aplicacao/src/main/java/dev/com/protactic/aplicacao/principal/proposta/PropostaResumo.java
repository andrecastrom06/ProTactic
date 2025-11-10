package dev.com.protactic.aplicacao.principal.proposta;

import java.util.Date;


public interface PropostaResumo {
    
    int getId();
    Integer getPropositorId();
    Integer getReceptorId();
    Integer getJogadorId();
    String getStatus();
    double getValor();
    Date getData();

}