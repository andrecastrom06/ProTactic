package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PartidaMock implements PartidaRepository {

    private final Map<Integer, Partida> partidas = new HashMap<>();
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public Optional<Partida> buscarPorId(Integer id) {
        return Optional.ofNullable(partidas.get(id));
    }

    @Override
    public Partida salvar(Partida partida) {
        if (partida.getId() == 0) {
            partida.setId(sequenceId.getAndIncrement());
        }
        partidas.put(partida.getId(), partida);
        return partida;
    }

    @Override
    public List<Partida> buscarPorMesEClube(Date data, Integer clubeId) {
        Calendar calBusca = Calendar.getInstance();
        calBusca.setTime(data);
        int mesBusca = calBusca.get(Calendar.MONTH);
        int anoBusca = calBusca.get(Calendar.YEAR);

        return partidas.values().stream()
            .filter(p -> {
                Calendar calP = Calendar.getInstance();
                calP.setTime(p.getDataJogo());
                return calP.get(Calendar.MONTH) == mesBusca && calP.get(Calendar.YEAR) == anoBusca;
            })
            .filter(p -> {
                // Verifica se o clube participou (Casa ou Visitante)
                Integer casaId = p.getClubeCasa() != null ? p.getClubeCasa().getId() : null;
                Integer visiId = p.getClubeVisitante() != null ? p.getClubeVisitante().getId() : null;
                return Objects.equals(casaId, clubeId) || Objects.equals(visiId, clubeId);
            })
            .collect(Collectors.toList());
    }

    public void limpar() {
        partidas.clear();
    }
}