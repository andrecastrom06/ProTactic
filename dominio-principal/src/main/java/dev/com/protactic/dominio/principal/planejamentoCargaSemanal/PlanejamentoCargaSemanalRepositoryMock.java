package dev.com.protactic.dominio.principal.planejamentoCargaSemanal;

import java.util.HashMap;
import java.util.Map;

import dev.com.protactic.dominio.principal.TreinoSemanal;

public class PlanejamentoCargaSemanalRepositoryMock {

    private final Map<String, TreinoSemanal> treinos = new HashMap<>();

    public void salvarTreino(TreinoSemanal treino) {
        String nome = treino.getJogador().getNome(); // usa getNome() da entidade Jogador
        treinos.put(nome, treino);
    }

    public TreinoSemanal buscarPorJogador(String nomeJogador) {
        return treinos.get(nomeJogador);
    }
}
