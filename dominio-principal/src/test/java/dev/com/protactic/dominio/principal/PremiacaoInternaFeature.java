package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import dev.com.protactic.mocks.PremiacaoMock;

public class PremiacaoInternaFeature {

    private PremiacaoMock mock;
    private Premiacao premiacao;

   

    @Dado("que {string} com média {string} no período de {string} existe")
    @Dado("{string} com média {string} no período de {string} existe")
    public void jogador_com_media_no_periodo_existe(String nome, String notaStr, String periodo) {
        double nota = Double.parseDouble(notaStr.replace(",", "."));

        if (mock == null) {
            mock = new PremiacaoMock();
            mock.clearJogadores();
        }

        mock.addJogador(nome, nota);

        System.out.println("DEBUG >> Criado jogador: " 
            + nome + " com média " + nota + " no período de " + periodo);
    }

    

    @Quando("eu criar a premiação do mês de {string}")
    public void eu_criar_a_premiacao(String mes) {
        premiacao = mock.criarPremiacao("Premiação " + mes, new Date());

        if (premiacao == null) {
            System.out.println("DEBUG >> Nenhum vencedor definido para o mês de " + mes);
        } else {
            System.out.println("DEBUG >> Vencedor provisório: "
                + premiacao.getJogador().getNome()
                + " (nota " + premiacao.getJogador().getNota()
                + ", desvio " + premiacao.getJogador().getDesvioPadrao() + ")");
        }
    }

    

    @Então("a premiação ficará sem vencedor")
    public void premiacao_sem_vencedor() {
        assertNull(premiacao, "Não deveria haver vencedor");
        System.out.println(" Nenhum vencedor encontrado");
    }

    @Então("o jogador {string} será definido como vencedor da premiação")
    public void vencedor_definido(String esperado) {
        assertNotNull(premiacao, "Deveria existir um vencedor");
        assertEquals(esperado, premiacao.getJogador().getNome(), "O vencedor não foi o esperado");
        System.out.println(" Vencedor: " + premiacao.getJogador().getNome());
    }

    @Então("o jogador com menor desvio padrão será definido como vencedor da premiação")
    public void vencedor_menor_desvio_padrao() {
        assertNotNull(premiacao, "Deveria existir um vencedor");

        double menorDesvio = mock.getJogadores().stream()
                .mapToDouble(Jogador::getDesvioPadrao)
                .min()
                .orElse(Double.MAX_VALUE);

        assertEquals(menorDesvio, premiacao.getJogador().getDesvioPadrao(),
                "O vencedor não foi o de menor desvio padrão");

        System.out.println(" Vencedor por desempate de desvio padrão: " 
            + premiacao.getJogador().getNome());
    }
}
