package dev.com.protactic.aplicacao.principal.lesao;


public interface LesaoResumo {
    
    int getId();
    Integer getJogadorId();
    boolean isLesionado(); 
    String getTempo();
    String getPlano();
    int getGrau();

}