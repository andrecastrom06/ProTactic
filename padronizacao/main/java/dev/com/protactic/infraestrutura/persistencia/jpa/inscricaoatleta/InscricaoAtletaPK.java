package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

import java.io.Serializable;
import java.util.Objects;

public class InscricaoAtletaPK implements Serializable {

    private String atleta;
    private String competicao;

    public InscricaoAtletaPK() {}

    public InscricaoAtletaPK(String atleta, String competicao) {
        this.atleta = atleta;
        this.competicao = competicao;
    }

    public String getAtleta() { return atleta; }
    public void setAtleta(String atleta) { this.atleta = atleta; }
    public String getCompeticao() { return competicao; }
    public void setCompeticao(String competicao) { this.competicao = competicao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InscricaoAtletaPK that = (InscricaoAtletaPK) o;
        return Objects.equals(atleta, that.atleta) &&
               Objects.equals(competicao, that.competicao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atleta, competicao);
    }
}