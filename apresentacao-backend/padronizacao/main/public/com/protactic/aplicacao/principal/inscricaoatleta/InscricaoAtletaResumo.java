package dev.com.protactic.aplicacao.principal.inscricaoatleta;


public interface InscricaoAtletaResumo {
    
    String getAtleta();
    String getCompeticao();
    boolean isElegivelParaJogos();
    boolean isInscrito();
    String getMensagemErro();

}