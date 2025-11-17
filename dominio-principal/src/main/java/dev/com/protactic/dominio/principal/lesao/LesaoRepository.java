package dev.com.protactic.dominio.principal.lesao;

import dev.com.protactic.dominio.principal.Lesao; // A sua entidade de domínio
import java.util.List;
import java.util.Optional;

public interface LesaoRepository {

   
    Lesao salvar(Lesao lesao);

    Optional<Lesao> buscarPorId(Integer id);

    List<Lesao> buscarTodasPorJogadorId(Integer jogadorId);

    Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId);
}