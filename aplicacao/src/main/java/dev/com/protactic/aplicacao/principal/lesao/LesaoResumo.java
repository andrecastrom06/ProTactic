package dev.com.protactic.aplicacao.principal.lesao;


public interface LesaoResumo {
    
    int getId();
    Integer getJogadorId();
    boolean isLesionado(); // O Spring Data entende 'isLesionado()' para booleanos
    String getTempo();
    String getPlano();
    int getGrau();

}