package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import dev.com.protactic.mocks.PremiacaoMock;

public class PremiacaoInternaFeature {

    private PremiacaoMock mock;
    private Premiacao premiacao;

    @Dado("que os jogadores {string} com média {string} e {string} com média {string} existem")
    public void que_os_jogadores_existem(String nome1, String nota1Str, String nome2, String nota2Str) {
        double nota1 = Double.parseDouble(nota1Str.replace(",", "."));
        double nota2 = Double.parseDouble(nota2Str.replace(",", "."));

        mock = new PremiacaoMock();
        mock.clearJogadores();
        mock.addJogador(nome1, nota1);
        mock.addJogador(nome2, nota2);

        System.out.println("DEBUG >> Criados jogadores:");
        mock.getJogadores().forEach(j ->
            System.out.println(" - " + j.getNome() + " com nota " + j.getNota())
        );
    }

    @Quando("eu criar a premiação do mês de {string}")
    public void eu_criar_a_premiacao(String mes) {
        premiacao = mock.criarPremiacao("Premiação " + mes, new Date());
        if (premiacao == null) {
            System.out.println("DEBUG >> Nenhum vencedor definido para o mês de " + mes);
        } else {
            System.out.println("DEBUG >> Vencedor provisório: " 
                + premiacao.getJogador().getNome() 
                + " (nota " + premiacao.getJogador().getNota() + ")");
        }
    }

    @Então("o jogador {string} será definido como vencedor da premiação")
    public void vencedor_definido(String esperado) {
        assertNotNull(premiacao, "Deveria existir um vencedor");
        assertEquals(esperado, premiacao.getJogador().getNome(), "O vencedor não foi o esperado");
        System.out.println("🏆 Vencedor: " + premiacao.getJogador().getNome());
    }

    @Então("a premiação ficará sem vencedor")
    public void premiacao_sem_vencedor() {
        assertNull(premiacao, "Não deveria haver vencedor");
        System.out.println("⚠ Nenhum vencedor encontrado");
    }
}
