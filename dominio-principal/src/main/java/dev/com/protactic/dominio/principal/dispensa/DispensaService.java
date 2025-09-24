package dev.com.protactic.dominio.principal.dispensa;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;

public class DispensaService {
    private final ContratoRepository contratoRepository;

    public DispensaService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public void dispensarJogador(Jogador jogador) throws Exception {
        if (jogador == null || jogador.getContrato() == null) {
            throw new Exception("Jogador não possui contrato ativo.");
        }

        if (!jogadorSaudavel(jogador)) {
            throw new Exception("Não é permitido dispensar jogadores que estão lesionados.");
        }

        jogador.getContrato().setStatus("RESCINDIDO");

        Clube passesLivres = new Clube("Passes Livres");
        jogador.setClube(passesLivres);

        contratoRepository.save(jogador.getContrato());
    }

    private boolean jogadorSaudavel(Jogador jogador) {
        return true;
    }
}