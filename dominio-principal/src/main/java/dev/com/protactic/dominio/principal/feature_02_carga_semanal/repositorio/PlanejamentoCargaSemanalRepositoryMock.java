package dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio;

import java.util.HashMap;
import java.util.Map;

import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.TreinoSemanal;

public class PlanejamentoCargaSemanalRepositoryMock {

    private final Map<String, TreinoSemanal> treinos = new HashMap<>();

    public void salvarTreino(TreinoSemanal treino) {
        String nome = treino.getJogador().getNome();
        treinos.put(nome, treino);
    }

    public TreinoSemanal buscarPorJogador(String nomeJogador) {
        return treinos.get(nomeJogador);
    }
}
