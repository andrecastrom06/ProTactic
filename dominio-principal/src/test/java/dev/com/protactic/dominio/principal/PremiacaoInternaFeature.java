package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import dev.com.protactic.mocks.PremiacaoMock;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;

public class PremiacaoInternaFeature {

    private PremiacaoMock mock = new PremiacaoMock();
    private Premiacao premiacao;
    private PremiacaoService service = new PremiacaoService();

    @Dado("que {string} com média {string} no período de {string} existe")
    @Dado("{string} com média {string} no período de {string} existe")
    public void jogador_com_media_no_periodo_existe(String nome, String notaStr, String periodo) {
        mock.registrarJogador(nome, notaStr); // ✅ delega conversão para o mock
    }

    @Quando("eu criar a premiação do mês de {string}")
    public void eu_criar_a_premiacao(String mes) {
        premiacao = mock.criarPremiacao("Premiação " + mes, new Date());
    }

    @Então("a premiação ficará sem vencedor")
    public void premiacao_sem_vencedor() {
        assertNull(premiacao, "Não deveria haver vencedor");
        assertNull(mock.getUltimaPremiacao(), "Nenhuma premiação deveria ter sido persistida");
    }

    @Então("o jogador {string} será definido como vencedor da premiação")
    public void vencedor_definido(String esperado) {
        assertNotNull(premiacao, "Deveria existir um vencedor");
        assertEquals(esperado, premiacao.getJogador().getNome(), "O vencedor não foi o esperado");
        assertEquals(esperado, mock.getUltimaPremiacao().getJogador().getNome());
    }

    @Então("o jogador com menor desvio padrão será definido como vencedor da premiação")
    public void vencedor_menor_desvio_padrao() {
        assertNotNull(premiacao, "Deveria existir um vencedor");
        assertTrue(service.verificarSeVencedorTemMenorDesvio(premiacao.getJogador(), mock.getJogadores()));
    }
}
