package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.Date;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Premiacao.
 * Contém apenas os dados necessários para listagens.
 */
public interface PremiacaoResumo {
    
    int getId();
    Integer getJogadorId();
    String getNome();
    Date getDataPremiacao();

}