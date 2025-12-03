package dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio;

import java.util.Date; 
import java.util.List;
import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;

public interface PartidaRepository {
    Optional<Partida> buscarPorId(Integer id);
    Partida salvar(Partida partida);
    List<Partida> buscarPorMesEClube(Date data, Integer clubeId);
}