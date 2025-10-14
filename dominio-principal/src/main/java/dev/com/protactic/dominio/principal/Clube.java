package dev.com.protactic.dominio.principal;

import java.util.ArrayList;
import java.util.List;

public class Clube {
    private int id;
    private Treinador treinador;
    private Analista analista;
    private Preparador preparador;
    private Competicao competicao;
    private String nome;
    private String cidadeEstado;
    private String estadio;
    private Jogador capitao;
    
    private List<Jogador> jogadores;

    public Clube(int id, Treinador treinador, Analista analista, Preparador preparador,
                 Competicao competicao, String nome, String cidadeEstado, String estadio,
                 Jogador capitao) {
        this.id = id;
        this.treinador = treinador;
        this.analista = analista;
        this.preparador = preparador;
        this.competicao = competicao;
        this.nome = nome;
        this.cidadeEstado = cidadeEstado;
        this.estadio = estadio;
        this.capitao = capitao;
        this.jogadores = new ArrayList<>();
    }

    public Clube(String nome) {
        this.nome = nome;
        this.jogadores = new ArrayList<>();
    }
    
    public void adicionarJogador(Jogador jogador) {
        if (jogador != null && !this.jogadores.contains(jogador)) {
            this.jogadores.add(jogador);
        }
        if (jogador.getClube() != this) {
            jogador.setClube(this);
        }
    }
    
    public void removerJogador(Jogador jogador) {
        if (jogador != null) {
            this.jogadores.remove(jogador);
        }
    }
    
    public boolean possuiJogador(String nomeJogador) {
        return this.jogadores.stream()
                .anyMatch(jogador -> jogador.getNome().equals(nomeJogador));
    }
    
    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Treinador getTreinador() { return treinador; }
    public void setTreinador(Treinador treinador) { this.treinador = treinador; }

    public Analista getAnalista() { return analista; }
    public void setAnalista(Analista analista) { this.analista = analista; }

    public Preparador getPreparador() { return preparador; }
    public void setPreparador(Preparador preparador) { this.preparador = preparador; }

    public Competicao getCompeticao() { return competicao; }
    public void setCompeticao(Competicao competicao) { this.competicao = competicao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidadeEstado() { return cidadeEstado; }
    public void setCidadeEstado(String cidadeEstado) { this.cidadeEstado = cidadeEstado; }

    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }

    public Jogador getCapitao() { return capitao; }
    public void setCapitao(Jogador capitao) { this.capitao = capitao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clube clube = (Clube) o;
        return nome != null && nome.equalsIgnoreCase(clube.nome);
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.toLowerCase().hashCode() : 0;
    }
}