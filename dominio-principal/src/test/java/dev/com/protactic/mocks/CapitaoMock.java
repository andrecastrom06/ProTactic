package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

import java.util.HashMap;
import java.util.Map;

public class CapitaoMock implements CapitaoRepository {

    private final Map<String, Jogador> capitoes = new HashMap<>();
    private Jogador ultimoCapitaoSalvo;

    @Override
    public void salvarCapitao(Jogador jogador) {
        capitoes.put(jogador.getClube().getNome(), jogador);
        this.ultimoCapitaoSalvo = jogador;
    }

    @Override
    public Jogador buscarCapitaoPorClube(String clube) {
        return capitoes.get(clube);
    }

    public Jogador getUltimoCapitaoSalvo() { 
        return ultimoCapitaoSalvo;
    }

    public void limpar() {
        capitoes.clear();
        ultimoCapitaoSalvo = null; 
    }
}
