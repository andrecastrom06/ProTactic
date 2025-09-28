package dev.com.protactic.dominio.principal.capitao;

import dev.com.protactic.dominio.principal.Jogador;
import java.util.HashMap;
import java.util.Map;

public class CapitaoRepositoryFake implements CapitaoRepository {
    private final Map<String, Jogador> mapa = new HashMap<>();

    @Override
    public void salvarCapitao(Jogador jogador) {
        if (jogador != null && jogador.getClube() != null) {
            mapa.put(jogador.getClube().getNome(), jogador);
        }
    }

    @Override
    public Jogador buscarCapitaoPorClube(String clube) {
        if (clube == null) return null;
        return mapa.get(clube);
    }
}
