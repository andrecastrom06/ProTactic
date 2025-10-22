package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.IClubeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClubeMock implements IClubeRepository {
    
    private final Map<Integer, Clube> clubes = new ConcurrentHashMap<>();
    
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public void salvar(Clube clube) {
        if (clube.getId() == 0) {
            clube.setId(sequenceId.getAndIncrement());
        }
        clubes.put(clube.getId(), clube);
    }

    @Override
    public Clube buscarPorNome(String nome) {
        return clubes.values().stream()
                .filter(clube -> clube.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Clube> listarTodos() {
        return new ArrayList<>(clubes.values());
    }

    @Override
    public Clube buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return clubes.get(id);
    }
}