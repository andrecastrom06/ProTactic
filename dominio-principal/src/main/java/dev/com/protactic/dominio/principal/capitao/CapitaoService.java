package dev.com.protactic.dominio.principal.capitao;

import dev.com.protactic.dominio.principal.Jogador;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CapitaoService {

    private final CapitaoRepository repository;

    public CapitaoService(CapitaoRepository repository) {
        this.repository = repository;
    }

    public boolean podeSerCapitao(Jogador jogador) {
        if (!jogador.isContratoAtivo()) return false;
        if (!"constante".equalsIgnoreCase(jogador.getMinutagem())) return false;
        return mesesNoClube(jogador) >= 12;
    }

    public long mesesNoClube(Jogador jogador) {
        if (jogador.getChegadaNoClube() == null) return 0;
        return ChronoUnit.MONTHS.between(jogador.getChegadaNoClube(), LocalDate.now());
    }

    public void definirCapitao(Jogador jogador) {
        jogador.setCapitao(true);
        repository.salvarCapitao(jogador);
    }

    public void definirCapitaoEntreJogadores(List<Jogador> jogadores) {
        Jogador melhor = null;
        for (Jogador j : jogadores) {
            if (!podeSerCapitao(j)) continue;
            if (melhor == null || mesesNoClube(j) > mesesNoClube(melhor)) {
                melhor = j;
            } else if (mesesNoClube(j) == mesesNoClube(melhor)) {
                // empate total -> não define ninguém
                melhor = null;
            }
        }
        if (melhor != null) {
            definirCapitao(melhor);
        }
    }
}
