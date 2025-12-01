package dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo;

import java.math.BigDecimal;

public class CalculoCargaLinear implements CalculadoraCargaStrategy {
    @Override
    public BigDecimal calcularCarga(double duracaoMinutos, double intensidade0a10) {
        // Fórmula simples: Duração * Intensidade
        return new BigDecimal(duracaoMinutos * intensidade0a10);
    }
}