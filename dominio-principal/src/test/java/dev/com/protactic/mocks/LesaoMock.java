package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LesaoMock implements LesaoRepository {
    
    private final Map<Integer, Lesao> lesoes = new HashMap<>();
    private int sequence = 1;

    @Override
    public Lesao salvar(Lesao lesao) {
        if (lesao.getId() == 0) {
            lesao.setId(sequence++);
        }
        lesoes.put(lesao.getId(), lesao);
        return lesao;
    }

    @Override
    public Optional<Lesao> buscarPorId(Integer id) {
        return Optional.ofNullable(lesoes.get(id));
    }

    @Override
    public List<Lesao> buscarTodasPorJogadorId(Integer jogadorId) {
        if (jogadorId == null) return List.of();
        return lesoes.values().stream()
            .filter(l -> l.getJogador() != null && jogadorId.equals(l.getJogador().getId()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId) {
        if (jogadorId == null) return Optional.empty();
        return lesoes.values().stream()
            .filter(l -> l.getJogador() != null && jogadorId.equals(l.getJogador().getId()) && l.isLesionado())
            .findFirst();
    }
    
    public void limpar() {
        lesoes.clear();
        sequence = 1;
    }
}