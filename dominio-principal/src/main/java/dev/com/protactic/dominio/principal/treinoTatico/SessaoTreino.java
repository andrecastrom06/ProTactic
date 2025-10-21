package dev.com.protactic.dominio.principal.treinoTatico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Partida;

public class SessaoTreino {
    private final String nome;
    private final Partida partida;
    private final List<Jogador> convocados;

    public SessaoTreino(String nome, Partida partida) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da sessão não pode ser vazio.");
        }
        if (partida == null) {
            throw new IllegalArgumentException("Sessão de treino precisa estar vinculada a uma partida.");
        }

        this.nome = nome;
        this.partida = partida;
        this.convocados = new ArrayList<>();
    }

    public void adicionarConvocado(Jogador jogador) {
        if (jogador != null && jogador.isDisponivel()) {
            convocados.add(jogador);
        }
    }

    public String getNome() {
        return nome;
    }

    public Partida getPartida() {
        return partida;
    }

    public List<Jogador> getConvocados() {
        return Collections.unmodifiableList(convocados);
    }
}
