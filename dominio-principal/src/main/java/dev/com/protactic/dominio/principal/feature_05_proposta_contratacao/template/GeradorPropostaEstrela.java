package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template;

import java.math.BigDecimal;

public class GeradorPropostaEstrela extends GeradorProposta {
    @Override
    protected BigDecimal calcularBonus(BigDecimal salarioBase) {
        // Estrela ganha 20% de luvas na assinatura
        return salarioBase.multiply(new BigDecimal("0.20"));
    }

    @Override
    protected BigDecimal calcularMulta(BigDecimal salarioBase) {
        // Multa de mercado (100x o salário)
        return salarioBase.multiply(new BigDecimal("100"));
    }
}