package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JogadorMock implements IJogadorRepository {
    
    // MUDANÇA: O mock agora usa o ID (Integer) como chave
    private final Map<Integer, Jogador> jogadores = new ConcurrentHashMap<>();
    
    // MUDANÇA: Simulador de AUTO_INCREMENT para os IDs
    private static final AtomicInteger sequenceId = new AtomicInteger(1);


    @Override
    public void salvar(Jogador jogador) {
        if (jogador.getId() == 0) {
            // Se for um jogador novo, atribui um ID
            jogador.setId(sequenceId.getAndIncrement());
        }
        // Salva no map usando o ID como chave
        jogadores.put(jogador.getId(), jogador);
    }

    @Override
    public Jogador buscarPorNome(String nome) {
        // MUDANÇA: Precisamos iterar
        return jogadores.values().stream()
                .filter(jogador -> jogador.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existe(String nome) {
        // MUDANÇA: Precisamos iterar
        return jogadores.values().stream()
                .anyMatch(jogador -> jogador.getNome().equalsIgnoreCase(nome));
    }

    @Override
    public List<Jogador> listarTodos() {
        return new ArrayList<>(jogadores.values());
    }

    // MUDANÇA: Implementação do método que faltava
    @Override
    public Jogador buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        // Agora a busca por ID é rápida
        return jogadores.get(id);
    }
}