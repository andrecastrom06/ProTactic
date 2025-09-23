package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class PremiacaoInternaFeature {

    private Jogador jogadorMaiorPontuacao;
    private Jogador jogadorSegundoLugar;
    private Premiacao premiacaoCriada;

    @Dado("que um jogador obteve a maior pontuação no mês de setembro, com nota média de {double}")
    public void jogador_com_maior_pontuacao_setembro(double nota) {
        this.jogadorMaiorPontuacao = new Jogador("Jogador Top");
        this.jogadorMaiorPontuacao.setNota(nota);
    }

    @Dado("que o jogador de maior pontuação no mês de outubro tem uma nota média de {double}")
    public void jogador_maior_outubro(double nota) {
        this.jogadorMaiorPontuacao = new Jogador("Jogador Outubro");
        this.jogadorMaiorPontuacao.setNota(nota);
    }

    @Dado("que um jogador obteve a segunda maior pontuação no mês de setembro")
    public void jogador_segunda_maior() {
        this.jogadorSegundoLugar = new Jogador("Jogador Secundário");
        this.jogadorSegundoLugar.setNota(7.5);
    }

    @Quando("o treinador for criar o prêmio de melhor jogador do time")
    public void criar_premio() {
        if (jogadorMaiorPontuacao != null) {
            System.out.println("DEBUG >> Jogador: " + jogadorMaiorPontuacao.getNome()
                    + " | Nota: " + jogadorMaiorPontuacao.getNota());
        }

        // Só cria prêmio se jogador existe e nota >= 6
        if (jogadorMaiorPontuacao != null && jogadorMaiorPontuacao.getNota() >= 6.0) {
            this.premiacaoCriada = new Premiacao(
                1,
                jogadorMaiorPontuacao,
                "Melhor Jogador do Time",
                new Date()
            );
        } else {
            this.premiacaoCriada = null;
        }
    }

    @Então("o prêmio será atribuído a este jogador")
    public void premio_atribuido() {
        assertNotNull(premiacaoCriada, "O prêmio deveria ter sido criado.");
        assertEquals(jogadorMaiorPontuacao, premiacaoCriada.getJogador(),
                "O prêmio deveria ser do jogador com maior pontuação.");
    }

    @Então("o prêmio não será atribuído a este jogador")
    public void premio_nao_atribuido() {
        assertNull(premiacaoCriada, "O prêmio não deveria ser atribuído.");
    }
}
