package dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio;

import java.util.List;
import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.Fisico;

public interface FisicoRepository {
    
    Fisico salvar(Fisico fisico);
    Optional<Fisico> buscarPorId(Integer id);
    List<Fisico> buscarPorJogadorId(Integer jogadorId);
}