package dev.com.protactic.aplicacao.principal.jogador;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Jogador.
 * Contém apenas os dados necessários para listagens.
 */
public interface JogadorResumo {
    
    int getId();
    String getNome();
    String getPosicao();
    double getNota();
    
    // NOTA: Mais tarde, podemos adicionar campos de
    // outros agregados aqui, como getNomeClube()
}