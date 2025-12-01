package dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo;

import java.math.BigDecimal;

/**
 * Interface Strategy para definir como a carga de treino é calculada.
 * Justificativa: Permite alterar o algoritmo de cálculo de desgaste físico
 * (Linear, Exponencial, Recuperação) sem alterar a classe de serviço.
 */
public interface CalculadoraCargaStrategy {
    BigDecimal calcularCarga(double duracaoMinutos, double intensidade0a10);
}