package dev.com.protactic.aplicacao.principal.jogador;

import java.time.LocalDate;

public interface JogadorResumo {

    int getId();
    String getNome();
    String getPosicao();
    double getNota();
    int getIdade();
    Integer getClubeId(); 
    
    String getStatus();         
    boolean isSaudavel();       
    int getGrauLesao();         
    LocalDate getChegadaNoClube(); 
    boolean isCapitao();       
    boolean isContratoAtivo();

}