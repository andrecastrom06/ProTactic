package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;

import java.util.*;

public class JogadorMock implements IJogadorRepository {
    private final Map<String, Jogador> jogadores = new HashMap<>();

    @Override
    public void salvar(Jogador jogador) {
        jogadores.put(jogador.getNome(), jogador);
    }

    @Override
    public Jogador buscarPorNome(String nome) {
        return jogadores.get(nome);
    }

    @Override
    public boolean existe(String nome) {
        return jogadores.containsKey(nome);
    }

    @Override
    public List<Jogador> listarTodos() {
        return new ArrayList<>(jogadores.values());
    }
}
