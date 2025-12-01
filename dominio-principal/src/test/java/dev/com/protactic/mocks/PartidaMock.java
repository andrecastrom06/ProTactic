package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PartidaMock implements PartidaRepository {

    private final Map<Integer, Partida> partidas = new HashMap<>();
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public Optional<Partida> buscarPorId(Integer id) {
        return Optional.ofNullable(partidas.get(id));
    }

    public Partida salvar(Partida partida) {
        if (partida.getId() == 0) {
            partida.setId(sequenceId.getAndIncrement());
        }
        partidas.put(partida.getId(), partida);
        return partida;
    }

    public void limpar() {
        partidas.clear();
    }
}