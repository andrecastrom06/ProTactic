package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator;

import java.math.BigDecimal;

public abstract class PremiacaoDecorator implements CalculadoraPremiacao {
    
    protected CalculadoraPremiacao calculadoraWrappada;

    public PremiacaoDecorator(CalculadoraPremiacao calculadora) {
        this.calculadoraWrappada = calculadora;
    }

    @Override
    public BigDecimal calcular() {
        return calculadoraWrappada.calcular();
    }
}