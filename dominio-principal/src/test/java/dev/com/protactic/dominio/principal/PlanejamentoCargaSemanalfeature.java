package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class PlanejamentoCargaSemanalfeature {

    private static class Jogador {
        private final String nome;
        // -1: saudável, 0: desconforto, 1-3: lesão
        int grauLesao = -1;
        boolean contratoAtivo = false;
        boolean treinoFoiRegistrado = false;

        Jogador(String nome) {
            this.nome = nome;
        }
    }


    private final Map<String, Jogador> jogadores = new HashMap<>();
    private String ultimoJogadorEmContexto;


    @Dado("que o preparador físico está na tela de planejamento de cargas semanais")
    public void preparador_esta_na_tela() {

    }

    @Dado("que o jogador {string} tem uma lesão de grau {int}")
    public void jogador_tem_lesao(String nome, Integer grau) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.grauLesao = grau;
        ultimoJogadorEmContexto = nome;
    }

    @Dado("que o jogador {string} está saudável")
    public void jogador_esta_saudavel(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.grauLesao = -1; // -1 representa um jogador saudável
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato ativo")
    public void possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.contratoAtivo = true;
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} não possui contrato ativo")
    public void nao_possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.contratoAtivo = false;
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato inativo")
    public void possui_contrato_inativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.contratoAtivo = false;
        ultimoJogadorEmContexto = nome;
    }


    private void tentarRegistrarTreinamento() {
        if (ultimoJogadorEmContexto == null) {
            fail("Nenhum jogador foi definido no contexto do cenário antes da ação.");
        }
        Jogador j = jogadores.get(ultimoJogadorEmContexto);

        // A lógica central da regra de negócio é aplicada aqui.
        boolean podeRegistrar = j.contratoAtivo && (j.grauLesao == -1 || j.grauLesao == 0);
        if (podeRegistrar) {
            j.treinoFoiRegistrado = true;
        }
    }

    @Quando("o preparador físico tentar registrar seu treinamento")
    public void o_preparador_fisico_tentar_registrar_seu_treinamento() {
        tentarRegistrarTreinamento();
    }

    @Quando("o preparador físico criar seus treinos semanais")
    public void o_preparador_fisico_criar_seus_treinos_semanais() {
        tentarRegistrarTreinamento();
    }

    @Então("{string} não poderá ter o treino registrado")
    public void nao_podera_ter_o_treino_registrado(String nome) {
        Jogador j = jogadores.get(nome);
        assertNotNull(j, "Jogador '" + nome + "' não foi encontrado no mapa de jogadores.");
        assertFalse(j.treinoFoiRegistrado, "Falha na verificação: O treino para '" + nome + "' foi registrado, mas não deveria.");
    }

    @Então("o treino será registrado na aba de {string}")
    public void o_treino_sera_registrado_na_aba_de(String nome) {
        Jogador j = jogadores.get(nome);
        assertNotNull(j, "Jogador '" + nome + "' não foi encontrado no mapa de jogadores.");
        assertTrue(j.treinoFoiRegistrado, "Falha na verificação: O treino para '" + nome + "' não foi registrado, mas deveria.");
    }
}

