package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.dominio.principal.dispensa.DispensaService;

public class Dispensa_jogadorFeature {

    private Jogador jogador;
    private Clube clube;
    private boolean estaSaudavel;
    private String mensagemErro;

    private DispensaService dispensaService = new DispensaService(new ContratoMock());

    @Dado("um jogador chamado {string} com contrato ativo com o {string}")
    public void um_jogador_chamado_com_contrato_ativo_com_o(String nome, String nomeClube) {
        this.clube = new Clube(nomeClube);
        Contrato contrato = new Contrato(clube);
        this.jogador = new Jogador(nome, clube);
        this.jogador.setContrato(contrato);
    }

    @E("o jogador está saudável")
    public void o_jogador_esta_saudavel() {
        this.estaSaudavel = true;
    }

    @E("o jogador está machucado")
    public void o_jogador_esta_machucado() {
        this.estaSaudavel = false;
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
        assertEquals(esperado, jogador.getClube().getNome());
    }

    @Então("o sistema deve bloquear a rescisão com a mensagem {string}")
    public void o_sistema_deve_bloquear_a_rescisao_com_a_mensagem(String mensagemEsperada) {
        assertEquals(mensagemEsperada, this.mensagemErro);
    }
}