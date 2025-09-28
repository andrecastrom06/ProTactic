package dev.com.protactic.dominio.principal.capitao;

import dev.com.protactic.dominio.principal.Jogador;

public class CapitaoService {

    private final CapitaoRepository repository;

    public CapitaoService(CapitaoRepository repository) {
        this.repository = repository;
    }
    
    public boolean definirCapitao(Jogador jogador) {
        if (jogador == null) return false;

        boolean contratoAtivo = false;
        if (jogador.getContrato() != null && jogador.getContrato().getStatus() != null) {
            contratoAtivo = jogador.getContrato().getStatus().equalsIgnoreCase("ATIVO")
                          || jogador.getContrato().getStatus().equalsIgnoreCase("ativo");
        }

        boolean tempoClubeOk = jogador.getAnosDeClube() >= 1;
        boolean minutagemOk = jogador.getMinutagem() != null &&
                              jogador.getMinutagem().equalsIgnoreCase("constante");

        if (contratoAtivo && tempoClubeOk && minutagemOk) {
            jogador.setCapitao(true);
            repository.salvarCapitao(jogador);
            return true;
        } else {
            jogador.setCapitao(false);
            return false;
        }
    }
}
