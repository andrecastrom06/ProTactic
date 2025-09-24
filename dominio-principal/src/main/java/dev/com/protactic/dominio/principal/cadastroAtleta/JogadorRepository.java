package dev.com.protactic.dominio.principal.cadastroAtleta;


import dev.com.protactic.dominio.principal.Jogador;
import java.util.*;

public class JogadorRepository {
    private final Map<String, Jogador> jogadores = new HashMap<>();

    public void salvar(Jogador jogador) {
        jogadores.put(jogador.getNome(), jogador);
    }

    public Jogador buscarPorNome(String nome) {
        return jogadores.get(nome);
    }

    public boolean existe(String nome) {
        return jogadores.containsKey(nome);
    }

    public List<Jogador> listarTodos() {
        return new ArrayList<>(jogadores.values());
    }
}
