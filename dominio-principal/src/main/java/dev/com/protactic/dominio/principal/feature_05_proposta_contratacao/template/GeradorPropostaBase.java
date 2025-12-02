package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template;

import java.math.BigDecimal;

public class GeradorPropostaBase extends GeradorProposta {
    @Override
    protected BigDecimal calcularBonus(BigDecimal salarioBase) {
        // Base não ganha bônus de assinatura (R$ 0.00)
        return BigDecimal.ZERO;
    }

    @Override
    protected BigDecimal calcularMulta(BigDecimal salarioBase) {
        // Multa alta para proteger o clube (200x o salário)
        return salarioBase.multiply(new BigDecimal("200"));
    }
}