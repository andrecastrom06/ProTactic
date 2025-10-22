package dev.com.protactic.dominio.principal;

import java.math.BigDecimal;

public class Nota {
    private String jogoId;
    private String jogadorId;
    private BigDecimal nota; 
    private String observacao;

    public Nota(String jogoId, String jogadorId, BigDecimal nota, String observacao) {
        this.jogoId = jogoId;
        this.jogadorId = jogadorId;
        this.nota = nota;
        this.observacao = observacao;
    }

    public String getJogoId() { return jogoId; }
    public String getJogadorId() { return jogadorId; }
    public BigDecimal getNota() { return nota; }
    public String getObservacao() { return observacao; }

    public void setJogoId(String jogoId) {
        this.jogoId = jogoId;
    }

    public void setJogadorId(String jogadorId) {
        this.jogadorId = jogadorId;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean temNota() { return nota != null; }
    public boolean temObservacao() {
        return observacao != null && !observacao.isBlank();
    }
}