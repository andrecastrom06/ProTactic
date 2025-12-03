package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class PremiacaoBase implements CalculadoraPremiacao {
    private BigDecimal valorBase;

    // Construtor que aceita o valor base inicial
    public PremiacaoBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    // Construtor padrão (caso necessário)
    public PremiacaoBase() {
        this.valorBase = new BigDecimal("500.00");
    }

    @Override
    public BigDecimal calcular() {
        return valorBase;
    }
}