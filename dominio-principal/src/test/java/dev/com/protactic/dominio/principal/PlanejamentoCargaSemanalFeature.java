package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalRepositoryMock;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.TreinoSemanal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class PlanejamentoCargaSemanalSteps {

    private final PlanejamentoCargaSemanalRepositoryMock repository = new PlanejamentoCargaSemanalRepositoryMock();
    private final PlanejamentoCargaSemanalService service = new PlanejamentoCargaSemanalService(repository);

    private final Map<String, Jogador> jogadores = new HashMap<>();
    private String ultimoJogadorEmContexto;

    @Dado("que o preparador físico está na tela de planejamento de cargas semanais")
    public void preparador_esta_na_tela() {
        // apenas contexto
    }

    @Dado("que o jogador {string} tem uma lesão de grau {int}")
    public void jogador_tem_lesao(String nome, Integer grau) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setGrauLesao(grau);
        ultimoJogadorEmContexto = nome;
    }

    @Dado("que o jogador {string} está saudável")
    public void jogador_esta_saudavel(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setGrauLesao(-1); // saudável
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato ativo")
    public void possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(true);
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} não possui contrato ativo")
    public void nao_possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(false);
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato inativo")
    public void possui_contrato_inativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(false);
        ultimoJogadorEmContexto = nome;
    }

    private void tentarRegistrarTreinamento() {
        Jogador j = jogadores.get(ultimoJogadorEmContexto);
        assertNotNull(j, "Nenhum jogador em contexto para registrar treino.");
        service.registrarTreino(j);
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
        TreinoSemanal treino = repository.buscarPorJogador(nome);
        assertNotNull(treino, "Treino não encontrado para " + nome);
        assertFalse(treino.isRegistrado(), "Falha: O treino de " + nome + " foi registrado, mas não deveria.");
    }

    @Então("o treino será registrado na aba de {string}")
    public void o_treino_sera_registrado_na_aba_de(String nome) {
        TreinoSemanal treino = repository.buscarPorJogador(nome);
        assertNotNull(treino, "Treino não encontrado para " + nome);
        assertTrue(treino.isRegistrado(), "Falha: O treino de " + nome + " não foi registrado, mas deveria.");
    }
}
