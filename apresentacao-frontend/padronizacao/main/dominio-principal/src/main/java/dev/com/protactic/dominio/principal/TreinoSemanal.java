package dev.com.protactic.dominio.principal;

public class TreinoSemanal {
    private final Jogador jogador;
    private boolean registrado;

    public TreinoSemanal(Jogador jogador) {
        this.jogador = jogador;
        this.registrado = false;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }
}
