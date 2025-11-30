package dev.com.protactic.dominio.principal.planejamentoFisico;

import dev.com.protactic.dominio.principal.Fisico;
import java.util.List;
import java.util.Optional;

public interface FisicoRepository {
    
    Fisico salvar(Fisico fisico);
    Optional<Fisico> buscarPorId(Integer id);
    List<Fisico> buscarPorJogadorId(Integer jogadorId);
}