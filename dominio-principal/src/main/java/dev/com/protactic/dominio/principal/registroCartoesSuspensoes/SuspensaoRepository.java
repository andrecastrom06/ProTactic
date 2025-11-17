package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import dev.com.protactic.dominio.principal.Suspensao;
import java.util.Optional;


public interface SuspensaoRepository {

    void salvarOuAtualizar(Suspensao suspensao);

    Optional<Suspensao> buscarPorAtleta(String atleta);
}