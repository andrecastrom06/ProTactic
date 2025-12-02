package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public class BonusVitoriaDecorator extends PremiacaoDecorator {

    public BonusVitoriaDecorator(CalculadoraPremiacao calculadora) {
        super(calculadora);
    }

    @Override
    public BigDecimal calcular() {
        // Adiciona R$ 500,00 se venceu
        return super.calcular().add(new BigDecimal("500.00"));
    }
}