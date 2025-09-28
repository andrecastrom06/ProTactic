package dev.com.protactic.dominio.principal.definirEsquemaTatico;

import java.util.List;

public class DefinirEsquemaTaticoService {

    private final EscalacaoRepository repository;

    public DefinirEsquemaTaticoService(EscalacaoRepository repository) {
        this.repository = repository;
    }

    /**
     * Tenta registrar um jogador na escalação do jogo.
     *
     * @param jogoData Data do jogo (ex: "24/08")
     * @param nomeJogador Nome do jogador
     * @param contratoAtivo Jogador tem contrato ativo?
     * @param suspenso Jogador está suspenso?
     * @param grauLesao -1 saudável, 0 desconforto, 1..3 lesão
     * @return true se conseguiu registrar, false caso inválido
     */
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
