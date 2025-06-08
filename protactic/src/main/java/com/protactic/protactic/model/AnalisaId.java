package com.protactic.protactic.model;

import java.io.Serializable;
import java.util.Objects;

public class AnalisaId implements Serializable {

    private String analista;
    private String jogador;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalisaId)) return false;
        AnalisaId that = (AnalisaId) o;
        return Objects.equals(analista, that.analista) &&
                Objects.equals(jogador, that.jogador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(analista, jogador);
    }
}