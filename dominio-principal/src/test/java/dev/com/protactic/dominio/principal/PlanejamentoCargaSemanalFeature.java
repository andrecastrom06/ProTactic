package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.TreinoSemanal;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.PlanejamentoCargaSemanalRepositoryMock;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoCargaSemanalService;
import dev.com.protactic.mocks.JogadorMock; 
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaLinear;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.Before; 

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class PlanejamentoCargaSemanalFeature {

    private PlanejamentoCargaSemanalRepositoryMock repository;
    private PlanejamentoCargaSemanalService service;
    private JogadorRepository jogadorRepo; 

    private final Map<String, Jogador> jogadores = new HashMap<>();
    private String ultimoJogadorEmContexto;

    @Before
    public void setup() {
        this.repository = new PlanejamentoCargaSemanalRepositoryMock();
        this.jogadorRepo = new JogadorMock(); 
        this.service = new PlanejamentoCargaSemanalService(repository, jogadorRepo); 
        this.jogadores.clear();
        this.ultimoJogadorEmContexto = null;
    }


    @Dado("que o preparador físico está na tela de planejamento de cargas semanais")
    public void preparador_esta_na_tela() {
       
    }

    @Dado("que o jogador {string} tem uma lesão de grau {int}")
    public void jogador_tem_lesao(String nome, Integer grau) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setGrauLesao(grau);
        jogadorRepo.salvar(j); 
        ultimoJogadorEmContexto = nome;
    }

    @Dado("que o jogador {string} está saudável")
    public void jogador_esta_saudavel(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setGrauLesao(-1);
        jogadorRepo.salvar(j); 
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato ativo")
    public void possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(true);
        jogadorRepo.salvar(j); 
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} não possui contrato ativo")
    public void nao_possui_contrato_ativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(false);
        jogadorRepo.salvar(j); 
        ultimoJogadorEmContexto = nome;
    }

    @Dado("{string} possui contrato inativo")
    public void possui_contrato_inativo(String nome) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        j.setContratoAtivo(false);
        jogadorRepo.salvar(j);
        ultimoJogadorEmContexto = nome;
    }

    private void tentarRegistrarTreinamento() {
        Jogador j = jogadores.get(ultimoJogadorEmContexto);
        assertNotNull(j, "Nenhum jogador em contexto para registrar treino.");
        
        jogadorRepo.salvar(j); 
        
        service.registrarTreino(j, 90.0, 7.0, new CalculoCargaLinear());
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
        TreinoSemanal treinoPersistido = repository.buscarPorJogador(nome);

        assertNotNull(treinoPersistido, "O repositório deveria conter o treino do jogador, mesmo que inválido.");
        assertFalse(treinoPersistido.isRegistrado(), "Falha: O treino de " + nome + " foi registrado, mas não deveria.");

        assertEquals(nome, treinoPersistido.getJogador().getNome(), 
            "O treino persistido não pertence ao jogador correto.");
    }

    @Então("o treino será registrado na aba de {string}")
    public void o_treino_sera_registrado_na_aba_de(String nome) {
        TreinoSemanal treinoPersistido = repository.buscarPorJogador(nome);

        assertNotNull(treinoPersistido, "Falha: Nenhum treino persistido encontrado para " + nome);
        assertTrue(treinoPersistido.isRegistrado(), 
            "Falha: O treino de " + nome + " não foi registrado, mas deveria.");

        assertEquals(nome, treinoPersistido.getJogador().getNome(),
            "O treino persistido não está vinculado ao jogador correto.");

        TreinoSemanal treinoBuscado = repository.buscarPorJogador(nome);
        assertEquals(treinoPersistido, treinoBuscado, 
            "Os dados persistidos no repositório não correspondem ao treino recuperado.");
    }
}