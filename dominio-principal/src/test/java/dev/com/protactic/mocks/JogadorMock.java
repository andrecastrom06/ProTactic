package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors; 

public class JogadorMock implements JogadorRepository {
    
    private final Map<Integer, Jogador> jogadores = new ConcurrentHashMap<>();
    
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public void salvar(Jogador jogador) {
        if (jogador.getId() == 0) {
            jogador.setId(sequenceId.getAndIncrement());
        }
        jogadores.put(jogador.getId(), jogador);
    }

    @Override
    public Jogador buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return jogadores.get(id);
    }

    @Override
    public List<Jogador> buscarPorClube(Integer clubeId) {
        return jogadores.values().stream()
                .filter(jogador -> clubeId.equals(jogador.getClubeId()))
                .collect(Collectors.toList());
    }
    @Override
    public List<Jogador> listarTodos() {
        return new ArrayList<>(jogadores.values());
    }

    public Jogador buscarPorNome(String nome) {
        return jogadores.values().stream()
                .filter(jogador -> jogador.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public boolean existe(String nome) {
        return jogadores.values().stream()
                .anyMatch(jogador -> jogador.getNome().equalsIgnoreCase(nome));
    }

    public List<Jogador> findByNomeIgnoreCase(String nome) {
        return jogadores.values().stream()
                .filter(jogador -> jogador.getNome().equalsIgnoreCase(nome))
                .collect(Collectors.toList());
    }
}