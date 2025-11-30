package dev.com.protactic.dominio.principal;

public class Lesao {
    private int id;
    private Jogador jogador;
    private boolean lesionado;
    private String tempo;
    private String plano;
    private int grau;

    public Lesao(int id, Jogador jogador, boolean lesionado, String tempo, String plano, int grau) {
        this.id = id;
        this.jogador = jogador;
        this.lesionado = lesionado;
        this.tempo = tempo;
        this.plano = plano;
        this.grau = grau;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public boolean isLesionado() { return lesionado; }
    public void setLesionado(boolean lesionado) { this.lesionado = lesionado; }

    public String getTempo() { return tempo; }
    public void setTempo(String tempo) { this.tempo = tempo; }

    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }

    public int getGrau() { return grau; }
    public void setGrau(int grau) { this.grau = grau; }
}