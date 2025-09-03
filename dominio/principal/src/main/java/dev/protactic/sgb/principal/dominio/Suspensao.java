package dev.protactic.sgb.principal.dominio;

public class Suspensao {
    private int id;
    private Jogador jogador;
    private boolean suspenso;
    private int amarelo;
    private int vermelho;

    public Suspensao(int id, Jogador jogador, boolean suspenso, int amarelo, int vermelho) {
        this.id = id;
        this.jogador = jogador;
        this.suspenso = suspenso;
        this.amarelo = amarelo;
        this.vermelho = vermelho;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public boolean isSuspenso() { return suspenso; }
    public void setSuspenso(boolean suspenso) { this.suspenso = suspenso; }

    public int getAmarelo() { return amarelo; }
    public void setAmarelo(int amarelo) { this.amarelo = amarelo; }

    public int getVermelho() { return vermelho; }
    public void setVermelho(int vermelho) { this.vermelho = vermelho; }
}
