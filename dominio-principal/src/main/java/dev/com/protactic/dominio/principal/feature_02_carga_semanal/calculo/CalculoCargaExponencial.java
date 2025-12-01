package dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo;

import java.math.BigDecimal;

public class CalculoCargaExponencial implements CalculadoraCargaStrategy {
    @Override
    public BigDecimal calcularCarga(double duracaoMinutos, double intensidade0a10) {
        // Fórmula: Duração * (Intensidade ^ 1.5)
        // Penaliza treinos muito intensos (bom para evitar lesões)
        double fatorIntensidade = Math.pow(intensidade0a10, 1.5);
        return new BigDecimal(duracaoMinutos * fatorIntensidade);
    }
}