package dev.com.protactic.aplicacao.principal.nota;

import java.math.BigDecimal;


public interface NotaResumo {
    
    String getJogoId();
    String getJogadorId();
    BigDecimal getNota();
    String getObservacao();

}