package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

public class DispensaJogadorFeature {

    private String jogador;
    private String clube;
    private boolean estaSaudavel;
    private String mensagemErro;

    @Dado("um jogador chamado {string} com contrato ativo com o {string}")
    public void um_jogador_chamado_com_contrato_ativo_com_o(String nome, String clube) {
        this.jogador = nome;
        this.clube = clube;
        this.mensagemErro = null;
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
        if (!estaSaudavel) {
            this.mensagemErro = "Não é permitido dispensar jogadores que estão lesionados.";
        } else {
            this.clube = "Passes Livres";
        }
    }

    @Então("o clube do jogador deve ser {string}")
    public void o_clube_do_jogador_deve_ser(String esperado) {
        assertEquals(esperado, this.clube);
        assertNotNull(this.jogador, "O nome do jogador deve estar definido.");
    }

    @Então("o sistema deve bloquear a rescisão com a mensagem {string}")
    public void o_sistema_deve_bloquear_a_rescisao_com_a_mensagem(String mensagemEsperada) {
        assertEquals(mensagemEsperada, this.mensagemErro);
    }
}