package dev.com.protactic.dominio.principal.partida;

import dev.com.protactic.dominio.principal.Partida;
import java.util.Optional;

public interface PartidaRepository {
    Optional<Partida> buscarPorId(Integer id);
    Partida salvar(Partida partida);
}