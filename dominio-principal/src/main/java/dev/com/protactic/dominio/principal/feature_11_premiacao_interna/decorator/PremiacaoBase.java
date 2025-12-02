package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class PremiacaoBase implements CalculadoraPremiacao {
    
    private BigDecimal valorBase;

    public PremiacaoBase(double valor) {
        this.valorBase = new BigDecimal(valor);
    }

    @Override
    public BigDecimal calcular() {
        return valorBase;
    }
}