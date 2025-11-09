package dev.com.protactic.aplicacao.principal.contrato;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Contrato.
 * Contém apenas os dados necessários para listagens.
 */
public interface ContratoResumo {
    
    int getId();
    int getDuracaoMeses();
    double getSalario();
    String getStatus();
    Integer getClubeId(); // Incluímos o Id do clube para referência

}