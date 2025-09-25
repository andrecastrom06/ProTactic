package dev.com.protactic.dominio.principal;

import java.math.BigDecimal;

public class Nota {
    private final String jogoId;
    private final String jogadorId;
    private final BigDecimal nota; 
    private final String observacao;

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

    public boolean temNota() { return nota != null; }
    public boolean temObservacao() {
        return observacao != null && !observacao.isBlank();
    }
}