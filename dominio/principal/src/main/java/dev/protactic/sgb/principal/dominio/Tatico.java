package dev.protactic.sgb.principal.dominio;

public class Tatico {
    private int id;
    private Jogador jogador;
    private Partida partida;
    private String categoria;
    private String nome;
    private String descricao;

    public Tatico(int id, Jogador jogador, Partida partida,
                  String categoria, String nome, String descricao) {
        this.id = id;
        this.jogador = jogador;
        this.partida = partida;
        this.categoria = categoria;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
