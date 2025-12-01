package dev.com.protactic.dominio.principal.feature_08_registro_cartoes.repositorio;

import java.util.List;
import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade.Suspensao;

public interface SuspensaoRepository {

    void salvarOuAtualizar(Suspensao suspensao);

    Optional<Suspensao> buscarPorAtleta(String atleta);
    
    List<Suspensao> buscarSuspensosPorClube(Integer clubeId);
}