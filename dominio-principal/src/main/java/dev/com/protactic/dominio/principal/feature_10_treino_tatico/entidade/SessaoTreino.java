package dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;

public class SessaoTreino {
    private int id;
    private final String nome;
    private final Partida partida;
    private final Integer clubeId; 
    private final List<Jogador> convocados;

    public SessaoTreino(String nome, Partida partida, Integer clubeId) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da sessão não pode ser vazio.");
        }
        if (partida == null) {
            throw new IllegalArgumentException("Sessão de treino precisa estar vinculada a uma partida.");
        }
        if (clubeId == null) {
            throw new IllegalArgumentException("Sessão de treino deve pertencer a um clube.");
        }
        this.nome = nome;
        this.partida = partida;
        this.clubeId = clubeId;
        this.convocados = new ArrayList<>();
    }
    
    public SessaoTreino(int id, String nome, Partida partida, Integer clubeId) {
        this(nome, partida, clubeId);
        this.id = id;
    }

    public void adicionarConvocado(Jogador jogador) {
        if (jogador != null) {
            convocados.add(jogador);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getClubeId() { return clubeId; }
    public String getNome() { return nome; }
    public Partida getPartida() { return partida; }
    public List<Jogador> getConvocados() { return Collections.unmodifiableList(convocados); }
}