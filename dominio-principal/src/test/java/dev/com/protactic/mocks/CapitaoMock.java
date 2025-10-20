package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

import java.util.HashMap;
import java.util.Map;

public class CapitaoMock implements CapitaoRepository {

    private final Map<String, Jogador> capitoes = new HashMap<>();

    @Override
    public void salvarCapitao(Jogador jogador) {
        capitoes.put(jogador.getClube().getNome(), jogador);
    }

    @Override
    public Jogador buscarCapitaoPorClube(String clube) {
        return capitoes.get(clube);
    }

    public void limpar() {
        capitoes.clear();
    }
}
