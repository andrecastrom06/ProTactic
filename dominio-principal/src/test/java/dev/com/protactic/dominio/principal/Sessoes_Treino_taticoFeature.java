package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

public class Sessoes_Treino_taticoFeature {
    @Dado("que o treinador está na área de planejamento de treinos")
    public void que_o_treinador_está_na_área_de_planejamento_de_treinos() {
    }

    @Dado("que existe o jogo {string} no calendário")
    public void que_existe_o_jogo_no_calendário(String nomeJogo) {
    }

    @Dado("que o jogador {string} tem o status {string}")
    public void que_o_jogador_tem_o_status(String nomeJogador, String status) {
    }

    @Dado("que já existe a sessão de treino {string} para o jogo {string}")
    public void que_já_existe_a_sessão_de_treino_para_o_jogo(String nomeSessao, String nomeJogo) {
    }

    @Dado("que não existem jogos no calendário")
    public void que_não_existem_jogos_no_calendário() {
    }

    @Quando("o treinador cria a sessão de treino {string} para o jogo {string}")
    public void o_treinador_cria_a_sessão_de_treino_para_o_jogo(String nomeSessao, String nomeJogo) {
    }

    @Quando("o treinador cria uma sessão de treino para o jogo {string}")
    public void o_treinador_cria_uma_sessão_de_treino_para_o_jogo(String nomeJogo) {

    }

    @Quando("o treinador tenta criar uma sessão de treino tático")
    public void o_treinador_tenta_criar_uma_sessão_de_treino_tático() {
    }

    @Então("o sistema deve permitir a criação da sessão")
    public void o_sistema_deve_permitir_a_criação_da_sessão() {
    }

    @Então("o sistema deve impedir a criação da sessão")
    public void o_sistema_deve_impedir_a_criação_da_sessão() {
    }

    @Então("o jogador {string} deve aparecer na lista de convocação")
    public void o_jogador_deve_aparecer_na_lista_de_convocação(String nomeJogador) {
    }
    
    @Então("o jogador {string} não deve aparecer na lista de convocação")
    public void o_jogador_não_deve_aparecer_na_lista_de_convocação(String nomeJogador) {
    }
    
    @Então("a sessão de treino {string} deve estar vinculada ao jogo {string}")
    public void a_sessão_de_treino_deve_estar_vinculada_ao_jogo(String nomeSessao, String nomeJogo) {
    }

    @Então("o sistema deve exibir a mensagem {string}")
    public void o_sistema_deve_exibir_a_mensagem(String mensagemEsperada) {
    }
}
