package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefinirEsquemaTaticofeature {

    // Classe interna para representar um Jogador
    private static class Jogador {
        final String nome;
        int grauLesao = -1; // -1: saudável, 0: desconforto, 1-3: lesão
        boolean contratoAtivo = false;
        boolean suspenso = false;

        Jogador(String nome) {
            this.nome = nome;
        }
    }

    // Classe interna para representar um Jogo
    private static class Jogo {
        final String data;
        List<String> escalacao = new ArrayList<>();
        boolean escalacaoRegistradaComSucesso = false;

        Jogo(String data) {
            this.data = data;
        }
    }

    private final Map<String, Jogador> jogadores = new HashMap<>();
    private final Map<String, Jogo> jogos = new HashMap<>();
    private String jogoEmContexto; // data do jogo em contexto

    @Dado("que o treinador está na tela de gerenciamento de escalação e tática")
    public void que_o_treinador_esta_na_tela_de_gerenciamento_de_escalacao_e_tatica() {
        System.out.println("Contexto: Treinador na tela de gerenciamento.");
    }

    @Dado("que existe um jogo marcado para {string}")
    public void que_existe_um_jogo_marcado_para(String data) {
        jogos.put(data, new Jogo(data));
        jogoEmContexto = data;
        System.out.println("Dado que existe um jogo marcado para " + data);
    }

    @Quando("o treinador cadastrar a escalação")
    public void o_treinador_cadastrar_a_escalacao() {
        Jogo jogo = jogos.get(jogoEmContexto);
        if (jogo != null) {
            jogo.escalacaoRegistradaComSucesso = true;
            System.out.println("Quando o treinador cadastra uma escalação válida.");
        } else {
            fail("Nenhum jogo foi definido no contexto do cenário.");
        }
    }

    @Então("a escalação aparecerá vinculada ao jogo do dia {string}")
    public void a_escalacao_aparecera_vinculada_ao_jogo_do_dia(String data) {
        Jogo jogo = jogos.get(data);
        assertNotNull(jogo, "Jogo do dia " + data + " não foi encontrado.");
        assertTrue(jogo.escalacaoRegistradaComSucesso, "Escalação não foi registrada com sucesso para o jogo do dia " + data);
        System.out.println("Então a escalação aparece vinculada ao jogo do dia " + data);
    }

    @E("o jogador {string} tem uma lesão de grau {int}")
    public void o_jogador_tem_uma_lesao_de_grau(String nomeJogador, Integer grau) {
        jogadores.computeIfAbsent(nomeJogador, Jogador::new).grauLesao = grau;
        System.out.println("E o jogador " + nomeJogador + " tem lesão de grau " + grau);
    }

    @E("{string} possui contrato ativo e não está suspenso")
    public void possui_contrato_ativo_e_nao_esta_suspenso(String nomeJogador) {
        Jogador j = jogadores.computeIfAbsent(nomeJogador, Jogador::new);
        j.contratoAtivo = true;
        j.suspenso = false;
        System.out.println("E " + nomeJogador + " possui contrato ativo e não está suspenso.");
    }

    private void tentarCadastrarEscalacao(String nomeJogador) {
        Jogo jogo = jogos.get(jogoEmContexto);
        Jogador jogador = jogadores.get(nomeJogador);

        if (jogo == null || jogador == null) {
            fail("Jogo ou Jogador não encontrado no contexto do teste para a escalação.");
            return;
        }

        // Lógica central da regra de negócio é aplicada aqui.
        boolean podeRegistrar = jogador.contratoAtivo && !jogador.suspenso && jogador.grauLesao <= 0;

        if (podeRegistrar) {
            jogo.escalacao.add(nomeJogador);
            jogo.escalacaoRegistradaComSucesso = true;
        } else {
            jogo.escalacaoRegistradaComSucesso = false;
        }
    }

    @Quando("o treinador cadastrar a escalação incluindo {string}")
    public void o_treinador_cadastrar_a_escalacao_incluindo(String nomeJogador) {
        tentarCadastrarEscalacao(nomeJogador);
        System.out.println("Quando o treinador tenta cadastrar a escalação incluindo " + nomeJogador);
    }

    @Então("a escalação será registrada com sucesso")
    public void a_escalacao_sera_registrada_com_sucesso() {
        Jogo jogo = jogos.get(jogoEmContexto);
        assertNotNull(jogo, "Jogo não encontrado no contexto.");
        assertTrue(jogo.escalacaoRegistradaComSucesso, "A escalação deveria ter sido registrada com sucesso, mas não foi.");
        System.out.println("Então a escalação é registrada com sucesso.");
    }

    @Então("a escalação não poderá ser registrada")
    public void a_escalacao_nao_podera_ser_registrada() {
        Jogo jogo = jogos.get(jogoEmContexto);
        assertNotNull(jogo, "Jogo não encontrado no contexto.");
        assertFalse(jogo.escalacaoRegistradaComSucesso, "A escalação não deveria ter sido registrada, mas foi.");
        System.out.println("Então a escalação não pode ser registrada.");
    }

    @E("o jogador {string} está suspenso")
    public void o_jogador_esta_suspenso(String nomeJogador) {
        jogadores.computeIfAbsent(nomeJogador, Jogador::new).suspenso = true;
        System.out.println("E o jogador " + nomeJogador + " está suspenso.");
    }

    @E("{string} possui contrato ativo e não está lesionado")
    public void possui_contrato_ativo_e_nao_esta_lesionado(String nomeJogador) {
        Jogador j = jogadores.computeIfAbsent(nomeJogador, Jogador::new);
        j.contratoAtivo = true;
        j.grauLesao = -1; // -1 indica sem lesão
        System.out.println("E " + nomeJogador + " possui contrato ativo e não está lesionado.");
    }

    @E("o jogador {string} tem a condição {string}")
    public void o_jogador_tem_a_condicao(String nome, String condicao) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        String condicaoLower = condicao.toLowerCase();

        if (condicaoLower.contains("lesão grau")) {
            j.grauLesao = Integer.parseInt(condicaoLower.replaceAll("\\D+", ""));
        } else if (condicaoLower.equals("suspenso")) {
            j.suspenso = true;
        } else if (condicaoLower.equals("saudável")) {
            j.grauLesao = -1;
            j.suspenso = false;
        }
        System.out.println("E o jogador " + nome + " tem a condição: " + condicao);
    }

    @E("{string} possui contrato {string}")
    public void possui_contrato(String nome, String statusContrato) {
        jogadores.computeIfAbsent(nome, Jogador::new).contratoAtivo = statusContrato.equalsIgnoreCase("ativo");
        System.out.println("E " + nome + " possui contrato " + statusContrato);
    }
}

