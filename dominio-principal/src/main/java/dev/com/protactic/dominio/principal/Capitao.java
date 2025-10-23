package dev.com.protactic.dominio.principal;

import java.util.Objects;

public class Capitao {

    private Integer id;
    private Jogador jogador; 
    private Integer clubeId;

    public Capitao(Jogador jogador) {
        this.jogador = jogador;
        this.clubeId = jogador.getClubeId();
    }

    public Integer getId() {
        return id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Integer getClubeId() {
        return clubeId;
    }

    public boolean pertenceAoClube(Integer clubeId) {
        return Objects.equals(this.clubeId, clubeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capitao)) return false;
        Capitao capitao = (Capitao) o;
        return Objects.equals(clubeId, capitao.clubeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubeId);
    }
}
