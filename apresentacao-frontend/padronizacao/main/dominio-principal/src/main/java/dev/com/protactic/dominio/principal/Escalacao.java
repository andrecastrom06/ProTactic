package dev.com.protactic.dominio.principal;

public class Escalacao {
    private int id;
    private Partida partida;
    private String esquema;
    private Jogador[] jogadores;

    public Escalacao(int id, Partida partida, String esquema, Jogador[] jogadores) {
        this.id = id;
        this.partida = partida;
        this.esquema = esquema;
        this.jogadores = jogadores;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }

    public String getEsquema() { return esquema; }
    public void setEsquema(String esquema) { this.esquema = esquema; }

    public Jogador[] getJogadores() { return jogadores; }
    public void setJogadores(Jogador[] jogadores) { this.jogadores = jogadores; }
}