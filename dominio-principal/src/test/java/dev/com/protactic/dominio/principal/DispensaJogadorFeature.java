package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.dominio.principal.dispensa.DispensaService;

public class DispensaJogadorFeature {

    private Jogador jogador;
    private Clube clube;
    private String mensagemErro;
    private ContratoMock contratoMock = new ContratoMock();
    private DispensaService dispensaService = new DispensaService(contratoMock);

    @Dado("um jogador chamado {string} com contrato ativo com o {string}")
    public void um_jogador_chamado_com_contrato_ativo_com_o(String nome, String nomeClube) {
        this.clube = new Clube(nomeClube);
        Contrato contrato = new Contrato(clube);
        contrato.setStatus("ATIVO");
        this.jogador = new Jogador(nome, clube);
        this.jogador.setContrato(contrato);
        contratoMock.clear(); 
    }

    @E("o jogador está saudável")
    public void o_jogador_esta_saudavel() {
        jogador.setSaudavel(true);
    }

    @E("o jogador está machucado")
    public void o_jogador_esta_machucado() {
        jogador.setSaudavel(false);
    }

    @Quando("o analista do {string} solicitar a rescisão do seu contrato")
    public void o_analista_do_solicitar_a_rescisao_do_seu_contrato(String clube) {
        try {
            dispensaService.dispensarJogador(jogador);
        } catch (Exception e) {
            this.mensagemErro = e.getMessage();
        }
    }

    @Então("o clube do jogador deve ser {string}")
    public void o_clube_do_jogador_deve_ser(String esperado) {
        assertEquals(esperado, jogador.getClube().getNome(), "O clube do jogador não foi atualizado corretamente");

        Contrato persistido = contratoMock.getUltimoContrato();
        assertNotNull(persistido, "O contrato deveria ter sido salvo no repositório");
        assertEquals("RESCINDIDO", persistido.getStatus(), "O contrato persistido deveria estar rescindido");
    }

    @Então("o sistema deve bloquear a rescisão com a mensagem {string}")
    public void o_sistema_deve_bloquear_a_rescisao_com_a_mensagem(String mensagemEsperada) {
        assertEquals(mensagemEsperada, this.mensagemErro, "Mensagem de erro incorreta");

        assertNull(contratoMock.getUltimoContrato(), "Nenhum contrato deveria ter sido salvo no repositório");
    }
}