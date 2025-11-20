package dev.com.protactic.mocks;

import java.util.*;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.EscalacaoRepository;

public class EscalacaoMock implements EscalacaoRepository {

    private final Map<Integer, Map<String, List<String>>> banco = new HashMap<>();

    @Override
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador, Integer clubeId) {
        banco.computeIfAbsent(clubeId, k -> new HashMap<>())
             .computeIfAbsent(jogoData, k -> new ArrayList<>())
             .add(nomeJogador);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData, Integer clubeId) {
        return banco.getOrDefault(clubeId, Collections.emptyMap())
                    .getOrDefault(jogoData, new ArrayList<>());
    }
}