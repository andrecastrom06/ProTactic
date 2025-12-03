package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class PremiacaoBase implements CalculadoraPremiacao {
    private BigDecimal valorBase;

    public PremiacaoBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public PremiacaoBase() {
        this.valorBase = new BigDecimal("500.00");
    }

    @Override
    public BigDecimal calcular() {
        return valorBase;
    }
}