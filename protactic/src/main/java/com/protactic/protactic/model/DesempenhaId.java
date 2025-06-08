package com.protactic.protactic.model;

import java.io.Serializable;
import java.util.Objects;

public class DesempenhaId implements Serializable {

    private String partida;
    private String jogador;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DesempenhaId)) return false;
        DesempenhaId that = (DesempenhaId) o;
        return Objects.equals(partida, that.partida) &&
                Objects.equals(jogador, that.jogador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partida, jogador);
    }
}
