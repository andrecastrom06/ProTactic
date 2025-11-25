package dev.com.protactic.apresentacao.principal.feature_09_atribuicao_notas;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversorNotaSingleton {

    private static final ConversorNotaSingleton INSTANCE = new ConversorNotaSingleton();

    private ConversorNotaSingleton() {
    }

    public static ConversorNotaSingleton getInstance() {
        return INSTANCE;
    }

    public BigDecimal validarEFormatar(BigDecimal notaAConverter) {
        if (notaAConverter == null) {
            throw new IllegalArgumentException("A nota não pode ser nula.");
        }

        if (notaAConverter.compareTo(BigDecimal.ZERO) < 0 || notaAConverter.compareTo(new BigDecimal("10.00")) > 0) {
            throw new IllegalArgumentException("A nota deve estar no intervalo de 0.00 a 10.00.");
        }

        return notaAConverter.setScale(2, RoundingMode.HALF_UP);
    }
}