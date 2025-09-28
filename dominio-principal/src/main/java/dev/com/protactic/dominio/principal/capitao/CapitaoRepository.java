package dev.com.protactic.dominio.principal.capitao;

import dev.com.protactic.dominio.principal.Jogador;

public interface CapitaoRepository {
    void salvarCapitao(Jogador jogador);
    Jogador buscarCapitaoPorClube(String clube);
}
