package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class BonusCapitaoDecorator implements CalculadoraPremiacao {
    private final CalculadoraPremiacao proximo;

    public BonusCapitaoDecorator(CalculadoraPremiacao proximo) {
        this.proximo = proximo;
    }

    @Override
    public BigDecimal calcular() {
        BigDecimal valorAtual = proximo.calcular();
        return valorAtual.add(valorAtual.multiply(new BigDecimal("0.20")));
    }
}