package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.Date;


public interface PremiacaoResumo {
    
    int getId();
    Integer getJogadorId();
    String getNome();
    Date getDataPremiacao();

}