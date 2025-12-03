package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class BonusVitoriaDecorator implements CalculadoraPremiacao {
    private final CalculadoraPremiacao proximo;

    public BonusVitoriaDecorator(CalculadoraPremiacao proximo) {
        this.proximo = proximo;
    }

    @Override
    public BigDecimal calcular() {
        return proximo.calcular().add(new BigDecimal("200.00"));
    }
}