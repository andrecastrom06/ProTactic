package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.cadastroAtleta.*;
import io.cucumber.java.pt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroDeAtletaFeature {

    private Clube meuClube;
    private Clube outroClube;
    private Jogador jogador;
    private boolean janelaAberta;
    private CadastroDeAtletaService cadastroDeAtletaService;
    private boolean resultadoContratacao;

    private IJogadorRepository jogadorRepo;
    private IClubeRepository clubeRepo;

    @Dado("que o ano atual é {int}")
    public void que_o_ano_atual_e(Integer ano) {
        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");

        this.jogadorRepo = new JogadorRepository();
        this.clubeRepo = new ClubeRepository();
        this.cadastroDeAtletaService = new CadastroDeAtletaService(jogadorRepo, clubeRepo);
    }

    @Dado("que um {string} com contrato ativo em outro clube existe")
    public void que_um_jogador_com_contrato_em_outro_clube_existe(String posicao) {
        this.jogador = new Jogador("Jogador_" + posicao);
        this.jogador.setPosicao(posicao);

        Contrato contratoExistente = new Contrato(outroClube);
        this.jogador.setContrato(contratoExistente);
        outroClube.adicionarJogador(this.jogador);

        assertNotNull(jogador.getContrato(), "O jogador deveria ter contrato ativo.");
    }

    @Dado("que um {string} sem contrato existe")
    public void que_um_jogador_sem_contrato_existe(String posicao) {
        this.jogador = new Jogador("Jogador_" + posicao);
        this.jogador.setPosicao(posicao);

        assertNull(jogador.getContrato(), "O jogador deveria estar sem contrato.");
    }

    @Dado("estamos em {int} de {word} \\(dentro da janela de transferências)")
    public void estamos_dentro_da_janela(Integer dia, String mes) {
        this.janelaAberta = true;
        System.out.println("Data: " + dia + " de " + mes + " (dentro da janela)");
    }

    @Dado("estamos em {int} de {word} \\(fora da janela de transferências)")
    public void estamos_fora_da_janela(Integer dia, String mes) {
        this.janelaAberta = false;
        System.out.println("Data: " + dia + " de " + mes + " (fora da janela)");
    }

    @Quando("eu tentar cadastrar esse atleta no meu clube")
    public void eu_tentar_cadastrar_esse_atleta_no_meu_clube() {
        this.resultadoContratacao = cadastroDeAtletaService.contratar(meuClube, jogador, this.janelaAberta);
    }

    @Então("não conseguirei realizar a contratação")
    public void nao_conseguirei_realizar_a_contratacao() {
        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        assertFalse(meuClube.possuiJogador(jogador.getNome()), "O jogador não deveria ter sido adicionado.");
        assertEquals(outroClube, jogador.getClube(), "O jogador deveria permanecer no clube original.");
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        assertTrue(this.resultadoContratacao, "A contratação deveria ter sido bem-sucedida.");
        assertTrue(meuClube.possuiJogador(jogador.getNome()), "O meu clube deveria ter o novo jogador.");
        assertEquals(meuClube, jogador.getClube(), "O clube do jogador deveria ser agora o meu clube.");
        assertNotNull(jogador.getContrato(), "Um novo contrato deveria ter sido criado.");
        assertEquals("ATIVO", jogador.getContrato().getStatus(), "O contrato deveria estar ativo.");
    }
}
