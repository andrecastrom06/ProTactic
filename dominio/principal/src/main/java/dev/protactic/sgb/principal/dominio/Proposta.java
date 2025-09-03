package dev.protactic.sgb.principal.dominio;

public class Proposta {
    private int id;
    private Clube propositor;
    private Clube receptor;
    private Jogador jogador;
    private String status;
    private double valor;

    public Proposta(int id, Clube propositor, Clube receptor, Jogador jogador, String status, double valor) {
        this.id = id;
        this.propositor = propositor;
        this.receptor = receptor;
        this.jogador = jogador;
        this.status = status;
        this.valor = valor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Clube getPropositor() { return propositor; }
    public void setPropositor(Clube propositor) { this.propositor = propositor; }

    public Clube getReceptor() { return receptor; }
    public void setReceptor(Clube receptor) { this.receptor = receptor; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
