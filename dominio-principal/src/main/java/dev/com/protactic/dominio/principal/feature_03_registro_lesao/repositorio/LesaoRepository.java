package dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio;

import java.util.List;
import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;

public interface LesaoRepository {

   
    Lesao salvar(Lesao lesao);

    Optional<Lesao> buscarPorId(Integer id);

    List<Lesao> buscarTodasPorJogadorId(Integer jogadorId);

    Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId);
}