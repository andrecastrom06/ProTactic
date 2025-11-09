package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import java.io.Serializable;
import java.util.Objects;

// 1. Esta classe representa a Chave Primária Composta
public class NotaPK implements Serializable {

    private String jogoId;
    private String jogadorId;

    // Construtor vazio obrigatório
    public NotaPK() {}

    // Construtor completo
    public NotaPK(String jogoId, String jogadorId) {
        this.jogoId = jogoId;
        this.jogadorId = jogadorId;
    }

    // Getters e Setters
    public String getJogoId() { return jogoId; }
    public void setJogoId(String jogoId) { this.jogoId = jogoId; }
    public String getJogadorId() { return jogadorId; }
    public void setJogadorId(String jogadorId) { this.jogadorId = jogadorId; }

    // 2. 'equals()' e 'hashCode()' são OBRIGATÓRIOS para chaves compostas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaPK notaPK = (NotaPK) o;
        return Objects.equals(jogoId, notaPK.jogoId) &&
               Objects.equals(jogadorId, notaPK.jogadorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jogoId, jogadorId);
    }
}