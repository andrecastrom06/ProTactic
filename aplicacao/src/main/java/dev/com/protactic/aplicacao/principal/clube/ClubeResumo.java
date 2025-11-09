package dev.com.protactic.aplicacao.principal.clube;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Clube.
 * Contém apenas os dados necessários para listagens.
 */
public interface ClubeResumo {
    
    int getId();
    String getNome();
    String getCidadeEstado();
    String getEstadio();
    
    // NOTA: No futuro, podemos adicionar o nome do treinador
    Integer getCapitaoId();
}