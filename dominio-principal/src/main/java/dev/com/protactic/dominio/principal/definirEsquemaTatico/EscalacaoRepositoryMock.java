package dev.com.protactic.dominio.principal.definirEsquemaTatico;

import java.util.*;

public class EscalacaoRepositoryMock implements EscalacaoRepository {

    private final Map<String, List<String>> banco = new HashMap<>();

    @Override
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador) {
        banco.computeIfAbsent(jogoData, k -> new ArrayList<>()).add(nomeJogador);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData) {
        return banco.getOrDefault(jogoData, new ArrayList<>());
    }
}
