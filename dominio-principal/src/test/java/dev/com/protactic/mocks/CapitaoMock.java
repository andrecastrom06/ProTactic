package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

import java.util.HashMap;
import java.util.Map;

public class CapitaoMock implements CapitaoRepository {

    private final Map<String, Jogador> capitães = new HashMap<>();

    @Override
    public void salvarCapitao(Jogador jogador) {
        if (jogador != null && jogador.getClube() != null && jogador.getClube().getNome() != null) {
            capitães.put(jogador.getClube().getNome(), jogador);
        }
    }

    @Override
    public Jogador buscarCapitaoPorClube(String nomeClube) {
        if (nomeClube == null) return null;
        return capitães.get(nomeClube);
    }

    public void limpar() {
        capitães.clear();
    }

    public boolean contemCapitao(String nomeClube) {
        return capitães.containsKey(nomeClube);
    }
}
