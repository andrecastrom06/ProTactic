package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.SuspensaoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SuspensaoMock implements SuspensaoRepository {

    private Map<String, Suspensao> bancoFake = new HashMap<>();

    @Override
    public void salvarOuAtualizar(Suspensao suspensao) {
        bancoFake.put(suspensao.getAtleta(), suspensao);
    }

    @Override
    public Optional<Suspensao> buscarPorAtleta(String atleta) {
        return Optional.ofNullable(bancoFake.get(atleta));
    }

    @Override
    public List<Suspensao> buscarSuspensosPorClube(Integer clubeId) {
        return new ArrayList<>();
    }
}