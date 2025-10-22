package dev.com.protactic.dominio.principal.definirEsquemaTatico;

import java.util.List;

public class DefinirEsquemaTaticoService {

    private final EscalacaoRepository repository;

    public DefinirEsquemaTaticoService(EscalacaoRepository repository) {
        this.repository = repository;
    }

    public boolean registrarEscalacao(String jogoData, String nomeJogador,
                                      boolean contratoAtivo, boolean suspenso, int grauLesao) {

        boolean valido = contratoAtivo && !suspenso && (grauLesao <= 0);

        if (valido) {
            repository.salvarJogadorNaEscalacao(jogoData, nomeJogador);
            return true;
        }
        return false;
    }

    public List<String> obterEscalacao(String jogoData) {
        return repository.obterEscalacaoPorData(jogoData);
    }
}
