package dev.com.protactic.apresentacao.principal.feature_12_dispensa_rescisao;

import dev.com.protactic.dominio.principal.dispensa.DispensaService;

/**
 * Comando Concreto para a operação de Dispensar Jogador.
 * O DispensaService é o Receptor (Receiver).
 */
public class DispensarJogadorComando implements Comando<Void> {

    private final DispensaService dispensaService;
    private final Integer jogadorId;

    public DispensarJogadorComando(DispensaService dispensaService, Integer jogadorId) {
        this.dispensaService = dispensaService;
        this.jogadorId = jogadorId;
    }

    @Override
    public Void executar() throws Exception { // <-- CORRIGIDO: throws Exception adicionado
        this.dispensaService.dispensarJogadorPorId(jogadorId);
        return null;
    }
}