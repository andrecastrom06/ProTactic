package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class BonusCapitaoDecorator extends PremiacaoDecorator {

    public BonusCapitaoDecorator(CalculadoraPremiacao calculadora) {
        super(calculadora);
    }

    @Override
    public BigDecimal calcular() {
        // Capitão ganha 20% extra sobre o valor acumulado
        BigDecimal valorAtual = super.calcular();
        BigDecimal bonus = valorAtual.multiply(new BigDecimal("0.20"));
        return valorAtual.add(bonus);
    }
}