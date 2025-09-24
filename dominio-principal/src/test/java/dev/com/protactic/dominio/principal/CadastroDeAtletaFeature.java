package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.dispensa.ContratacaoServico;
import io.cucumber.java.pt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroDeAtletaFeature {

    private Clube meuClube;
    private Clube outroClube;
    private Jogador jogador;
    private boolean janelaAberta;
    private ContratacaoServico contratacaoServico;
    private boolean resultadoContratacao;

    @Dado("que o ano atual é {int}")
    public void que_o_ano_atual_e(Integer ano) {
        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");
        this.contratacaoServico = new ContratacaoServico();
        System.out.println("Contexto: Ano atual é " + ano);
    }

    // --- Cenário 1: Contratar fora da janela ---
    @Dado("que um lateral direito com contrato em outro clube existe")
    public void que_um_lateral_direito_com_contrato_em_outro_clube_existe() {
        this.jogador = new Jogador("Léo Destro");
        this.jogador.setPosicao("Lateral Direito");

        // cria contrato ativo no outro clube
        Contrato contratoExistente = new Contrato(outroClube);
        this.jogador.setContrato(contratoExistente);
        outroClube.adicionarJogador(this.jogador);

        assertNotNull(jogador.getContrato(), "O jogador deveria ter um contrato.");
        assertEquals("ATIVO", jogador.getContrato().getStatus(), "O contrato deveria estar ativo.");
    }

    @Dado("está dentro da janela de transferência")
    public void esta_dentro_da_janela_de_transferencia() {
        // aqui você decide: se o cenário for para simular fora da janela, use false
        // se for dentro da janela, use true
        this.janelaAberta = false; // no cenário 1 é fora da janela
    }

    // --- Cenário 2: Jogador livre dentro da janela ---
    @Dado("que um zagueiro sem contrato existe no dia {int} de outubro")
    public void que_um_zagueiro_sem_contrato_existe_no_dia_de_outubro(Integer dia) {
        this.jogador = new Jogador("David Muro");
        this.jogador.setPosicao("Zagueiro");
        this.janelaAberta = true; // esse cenário deve simular janela aberta
        assertNull(jogador.getContrato(), "O jogador deveria estar sem contrato.");
    }

    // --- Step compartilhado entre os cenários ---
    @Quando("eu tentar cadastrar esse atleta no meu clube")
    public void eu_tentar_cadastrar_esse_atleta_no_meu_clube() {
        this.resultadoContratacao = contratacaoServico.registrarAtleta(meuClube, jogador, this.janelaAberta);
    }

    // --- Validações ---
    @Então("não conseguirei realizar a contratação")
    public void nao_conseguirei_realizar_a_contratacao() {
        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        assertFalse(meuClube.possuiJogador(jogador.getNome()), "O jogador não deveria ter sido adicionado ao meu clube.");
        assertEquals(outroClube, jogador.getClube(), "O jogador deveria permanecer em seu clube original.");
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        assertTrue(this.resultadoContratacao, "A contratação do jogador livre deveria ter sido bem-sucedida.");
        assertTrue(meuClube.possuiJogador(jogador.getNome()), "O meu clube deveria ter o novo jogador na lista.");
        assertEquals(meuClube, jogador.getClube(), "O clube do jogador deveria ser agora o meu clube.");
        assertNotNull(jogador.getContrato(), "Um novo contrato deveria ter sido criado para o jogador.");
        assertEquals("ATIVO", jogador.getContrato().getStatus(), "O novo contrato deveria estar ativo.");
    }
}
