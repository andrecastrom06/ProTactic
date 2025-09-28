package dev.com.protactic.dominio.principal.definirEsquemaTatico;

import java.util.List;

public interface EscalacaoRepository {
    void salvarJogadorNaEscalacao(String jogoData, String nomeJogador);
    List<String> obterEscalacaoPorData(String jogoData);
}
