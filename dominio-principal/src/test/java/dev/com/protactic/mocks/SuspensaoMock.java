package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.SuspensaoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação "Mock" (de teste) para o SuspensaoRepository.
 * Simula um banco de dados em memória usando um Map.
 */
public class SuspensaoMock implements SuspensaoRepository {

    // Simula a tabela 'suspensao' em memória
    private Map<String, Suspensao> bancoFake = new HashMap<>();

    @Override
    public void salvarOuAtualizar(Suspensao suspensao) {
        // Simplesmente salva ou substitui o estado do atleta no mapa
        bancoFake.put(suspensao.getAtleta(), suspensao);
    }

    @Override
    public Optional<Suspensao> buscarPorAtleta(String atleta) {
        // Busca o atleta no mapa
        return Optional.ofNullable(bancoFake.get(atleta));
    }
}