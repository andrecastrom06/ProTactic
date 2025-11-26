package dev.com.protactic.apresentacao.principal.feature_12_dispensa_rescisao;

import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.contrato.ContratoService;

public class RenovarContratoComando implements Comando<Contrato> {

    private final ContratoService contratoService;
    private final Integer contratoId;
    private final int duracaoMeses;
    private final double salario;
    private final String status;

    public RenovarContratoComando(ContratoService contratoService, Integer contratoId, int duracaoMeses, double salario, String status) {
        this.contratoService = contratoService;
        this.contratoId = contratoId;
        this.duracaoMeses = duracaoMeses;
        this.salario = salario;
        this.status = status;
    }

    @Override
    public Contrato executar() throws Exception { 
        return contratoService.renovarContrato(
            contratoId,
            duracaoMeses,
            salario,
            status
        );
    }
}