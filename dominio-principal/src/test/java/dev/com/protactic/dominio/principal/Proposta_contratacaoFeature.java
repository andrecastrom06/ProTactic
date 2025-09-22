package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Locale;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.E;

public class Proposta_contratacaoFeature {
    @Dado("um jogador chamado {string} que não tem contrato")
    public void um_jogador_chamado_que_não_tem_contrato(String nomeJogador) {
        // Implementação do passo
    }

    @Dado("um jogador chamado {string} que tem contrato ativo com o {string}")
    public void um_jogador_chamado_que_tem_contrato_ativo_com_o(String nomeJogador, String nomeClube) {
        // Implementação do passo
    }

    @Dado("a data é {string}")
    public void a_data_é(String data) throws ParseException {
        Locale locale = new Locale("pt", "BR");
        // Implementação do passo
    }

    @Quando("um analista do {string} cria uma proposta de contrato para {string}")
    public void um_analista_do_cria_uma_proposta_de_contrato_para(String nomeClube, String nomeJogador) {
        // Implementação do passo
    }
    
    @Então("a proposta deve ser registrada com sucesso")
    public void a_proposta_deve_ser_registrada_com_sucesso() {
        // Implementação do passo
    }
    
    @Então("o sistema deve lançar uma exceção com a mensagem {string}")
    public void o_sistema_deve_lançar_uma_exceção_com_a_mensagem(String mensagemEsperada) {
        // Implementação do passo
    }

    //TESTE1 = D1, Q1, E1
    //TESTE2 = D2, Q1, E2
    //TESTE3 = D2, D3, Q1, E1
    //TESTE4 = D2, D3, Q1, E2
}