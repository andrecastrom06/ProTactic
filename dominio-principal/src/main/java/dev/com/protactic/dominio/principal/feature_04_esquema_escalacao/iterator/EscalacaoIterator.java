package dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.iterator;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import java.util.NoSuchElementException;

public class EscalacaoIterator implements JogadorIterator {
    // Referência para a coleção a ser iterada (o Array de Jogadores)
    private final Jogador[] jogadores;
    // Posição atual para controle da iteração
    private int posicaoAtual = 0;

    public EscalacaoIterator(Jogador[] jogadores) {
        this.jogadores = jogadores;
    }

    @Override
    public boolean existeProximo() {
        // Verifica se a posição atual está dentro dos limites do array
        return posicaoAtual < jogadores.length;
    }

    @Override
    public Jogador proximo() {
        if (!existeProximo()) {
            throw new NoSuchElementException("Não há mais jogadores na escalação.");
        }
        // Retorna o jogador na posição atual e avança o contador
        return jogadores[posicaoAtual++];
    }

    @Override
    public void resetar() {
        posicaoAtual = 0;
    }
}