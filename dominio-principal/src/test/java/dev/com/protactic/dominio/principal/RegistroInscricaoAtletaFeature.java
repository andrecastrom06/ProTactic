package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.entidade.InscricaoAtleta;
import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.servico.RegistroInscricaoService;
import dev.com.protactic.mocks.RegistroInscricaoMock;
import dev.com.protactic.mocks.JogadorMock;
import dev.com.protactic.mocks.RegistroLesoesMock;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class RegistroInscricaoAtletaFeature {

    private RegistroInscricaoService service;
    private InscricaoAtleta inscricaoResult;
    private Exception excecaoOcorrida;
    
    private RegistroInscricaoMock inscricaoMock;
    private JogadorRepository jogadorRepo;
    private RegistroLesoesRepository lesoesRepo;

    @Before
    public void setup() {
        this.inscricaoMock = new RegistroInscricaoMock();
        this.jogadorRepo = new JogadorMock();
        this.lesoesRepo = new RegistroLesoesMock(); 

        this.service = new RegistroInscricaoService(
            this.inscricaoMock,
            this.jogadorRepo,
            this.lesoesRepo
        );

        this.inscricaoResult = null;
        this.excecaoOcorrida = null; 
    }

    private void setupJogador(String nome, int idade, boolean contratoAtivo) {
        Jogador j = new Jogador(nome);
        j.setIdade(idade);
        j.setContratoAtivo(contratoAtivo);
        this.jogadorRepo.salvar(j);
        
        this.lesoesRepo.cadastrarAtleta(nome);
        this.lesoesRepo.definirContratoAtivo(nome, contratoAtivo);
    }

    @Dado("que João possui contrato ativo e tem {int} anos")
    public void que_joao_possui_contrato_ativo_e_tem_anos(Integer idade) {
        setupJogador("João", idade, true);
    }
    @Quando("o analista registrar a inscrição de João na competição {string}")
    public void o_analista_registrar_a_inscricao_de_joao_na_competicao(String competicao) {
        try {
            inscricaoResult = service.registrarInscricaoPorNome("João", competicao);
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Então("João fica inscrito na competição {string}")
    public void joao_fica_inscrito_na_competicao(String competicaoEsperada) {
        assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção"); 
        assertNotNull(inscricaoResult, "O resultado da inscrição não pode ser nulo");
        assertEquals("João", inscricaoResult.getAtleta());
        assertEquals(competicaoEsperada, inscricaoResult.getCompeticao());
        assertTrue(inscricaoResult.isInscrito());
        assertNull(inscricaoResult.getMensagemErro());

        InscricaoAtleta inscricaoSalva = inscricaoMock.buscarPorAtletaECompeticao("João", competicaoEsperada);
        assertNotNull(inscricaoSalva, "Inscrição de João deve estar persistida no repositório");
        assertTrue(inscricaoSalva.isInscrito());
    }

    @Então("passa a estar elegível para jogos dessa competição")
    public void jogador_elegivel_para_jogos() {
        assertTrue(inscricaoResult.isElegivelParaJogos());
    }

    @Dado("que Pedro possui contrato ativo e tem {int} anos")
    public void que_pedro_possui_contrato_ativo_e_tem_anos(Integer idade) {
        setupJogador("Pedro", idade, true);
    }

    @Quando("o analista tentar registrar a inscrição de Pedro na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_pedro_na_competicao(String competicao) {
        try {
            inscricaoResult = service.registrarInscricaoPorNome("Pedro", competicao);
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Então("o sistema não permite o registro")
    public void sistema_nao_permite_registro() {
        assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção"); 
        assertNotNull(inscricaoResult, "O resultado da inscrição não pode ser nulo");
        assertFalse(inscricaoResult.isInscrito());
        assertNotNull(inscricaoResult.getMensagemErro());

        InscricaoAtleta inscricaoSalva = inscricaoMock.buscarPorAtletaECompeticao(
            inscricaoResult.getAtleta(), 
            inscricaoResult.getCompeticao() 
        );
        
        assertNotNull(inscricaoSalva, "Inscrição deve estar persistida no repositório (mesmo com falha)");
        assertFalse(inscricaoSalva.isInscrito());
        assertNotNull(inscricaoSalva.getMensagemErro());
    }

    @Então("o sistema exibe o erro {string}")
    public void sistema_exibe_erro(String mensagemEsperada) {
        assertEquals(mensagemEsperada, inscricaoResult.getMensagemErro());
    }

    @Dado("que Lucas tem {int} anos mas não possui contrato ativo")
    public void que_lucas_tem_anos_mas_nao_possui_contrato_ativo(Integer idade) {
        setupJogador("Lucas", idade, false);
    }


    @Quando("o analista tentar registrar a inscrição de Lucas na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_lucas_na_competicao(String competicao) {
        try {
            inscricaoResult = service.registrarInscricaoPorNome("Lucas", competicao);
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }
}