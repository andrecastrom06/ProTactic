package dev.com.protactic.aplicacao.principal.inscricaoatleta;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade InscricaoAtleta.
 * Contém apenas os dados necessários para listagens.
 */
public interface InscricaoAtletaResumo {
    
    String getAtleta();
    String getCompeticao();
    boolean isElegivelParaJogos();
    boolean isInscrito();
    String getMensagemErro();

}