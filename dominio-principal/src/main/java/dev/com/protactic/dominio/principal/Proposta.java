package dev.com.protactic.dominio.principal;

public class Proposta {
    private int id;
    private Clube propositor;
    private Clube receptor;
    private Jogador jogador;
    private String status;
    private double valor;
    private java.util.Date data;

    public Proposta(int id, Clube propositor, Clube receptor, Jogador jogador, String status, double valor, java.util.Date data) {
        this.id = id;
        this.propositor = propositor;
        this.receptor = receptor;
        this.jogador = jogador;
        this.status = status;
        this.valor = valor;
        this.data = data;
    }

    public Proposta(Clube propositor) {
        this.propositor = propositor;
    }

    public Proposta(Jogador jogador, Clube propositor, java.util.Date data) {
        if (jogador.getContrato() != null && "ATIVO".equals(jogador.getContrato().getStatus())) {
            if (!jogador.getContrato().getClube().getNome().equals(propositor.getNome())) {
                if (!isPeriodoTransferencia(data)) {
                    throw new IllegalArgumentException("Jogador não pode ser contratado fora do prazo de transferência.");
                }
            } else {
                throw new IllegalArgumentException("Jogador já possui contrato ativo com o clube.");
            }
        }
        this.jogador = jogador;
        this.propositor = propositor;
        this.data = data;
    }

    private boolean isPeriodoTransferencia(java.util.Date data) {
        @SuppressWarnings("deprecation")
        int mes = data.getMonth(); // 0 = janeiro, 6 = julho
        return mes == 0 || mes == 6 || mes == 1 || mes == 7;
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

    public java.util.Date getData() { return data; }
    public void setData(java.util.Date data) { this.data = data; }
}