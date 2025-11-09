package dev.com.protactic.aplicacao.principal.lesao;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Lesao.
 * Contém apenas os dados necessários para listagens.
 */
public interface LesaoResumo {
    
    int getId();
    Integer getJogadorId();
    boolean isLesionado(); // O Spring Data entende 'isLesionado()' para booleanos
    String getTempo();
    String getPlano();
    int getGrau();

}