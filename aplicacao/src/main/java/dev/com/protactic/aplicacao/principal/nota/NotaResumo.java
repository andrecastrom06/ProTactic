package dev.com.protactic.aplicacao.principal.nota;

import java.math.BigDecimal;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Nota.
 * Contém os dados para listagens.
 */
public interface NotaResumo {
    
    String getJogoId();
    String getJogadorId();
    BigDecimal getNota();
    String getObservacao();

}