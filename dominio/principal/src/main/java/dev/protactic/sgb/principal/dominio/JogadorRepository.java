package dev.protactic.sgb.principal.dominio;

import java.util.Optional;

public interface JogadorRepository {
    void salvar(Jogador jogador);

    Jogador obter(JogadorId id);

    Optional<Jogador> findByNome(String nome);
}