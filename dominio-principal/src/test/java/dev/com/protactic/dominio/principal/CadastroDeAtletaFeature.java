package dev.com.protactic.dominio.principal;


import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.servico.CadastroDeAtletaService;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.mocks.JogadorMock;
import dev.com.protactic.mocks.ContratoMock;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class CadastroDeAtletaFeature {

    private Clube meuClube;
    private Clube outroClube;
    private Jogador jogador;
    private CadastroDeAtletaService cadastroDeAtletaService;
    private boolean resultadoContratacao;
    private Date dataAtual;

    private JogadorRepository jogadorRepo;
    private ClubeRepository clubeRepo;
    private ContratoRepository contratoRepo;

    @Before
    public void setup() {
        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeMock();
        this.contratoRepo = new ContratoMock();
        
        this.cadastroDeAtletaService = new CadastroDeAtletaService(jogadorRepo, clubeRepo, contratoRepo);

        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");
        this.clubeRepo.salvar(meuClube);
        this.clubeRepo.salvar(outroClube);
    }

    @Dado("que {string} com contrato ativo em outro clube existe")
    public void que_jogador_com_contrato_em_outro_clube_existe(String nomeJogador) {
        
        this.jogador = new Jogador(nomeJogador);
        this.jogadorRepo.salvar(this.jogador);

        Contrato contratoExistente = new Contrato(outroClube.getId());
        this.contratoRepo.salvar(contratoExistente);

        this.jogador.setContratoId(contratoExistente.getId());
        this.jogador.setClubeId(outroClube.getId());

        this.outroClube.adicionarJogadorId(this.jogador.getId());
        
        this.jogadorRepo.salvar(this.jogador);
        this.clubeRepo.salvar(this.outroClube);
    }

    @Dado("que {string} sem contrato existe")
    public void que_jogador_sem_contrato_existe(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);
        this.jogadorRepo.salvar(this.jogador);
    }

    @Dado("estamos em {int} de {word} \\(dentro da janela de transferências)")
    public void estamos_dentro_da_janela(Integer dia, String mes) {
        this.dataAtual = criarData(dia, mes, 2025);
    }

    @Dado("estamos em {int} de {word} \\(fora da janela de transferências)")
    public void estamos_fora_da_janela(Integer dia, String mes) {
        this.dataAtual = criarData(dia, mes, 2025);
    }

    private Date criarData(int dia, String mesNome, int ano) {
        Map<String, Integer> meses = new HashMap<>();
        meses.put("janeiro", 1);
        meses.put("fevereiro", 2);
        meses.put("março", 3);
        meses.put("abril", 4);
        meses.put("maio", 5);
        meses.put("junho", 6);
        meses.put("julho", 7);
        meses.put("agosto", 8);
        meses.put("setembro", 9);
        meses.put("outubro", 10);
        meses.put("novembro", 11);
        meses.put("dezembro", 12);

        int mes = meses.get(mesNome.toLowerCase());

        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes - 1, dia, 0, 0, 0);
        return cal.getTime();
    }

    @Quando("eu tentar cadastrar esse atleta no meu clube")
    public void eu_tentar_cadastrar_esse_atleta_no_meu_clube() {
        this.resultadoContratacao = cadastroDeAtletaService.contratar(meuClube, jogador, this.dataAtual);
    }

    @Então("não conseguirei realizar a contratação")
    public void nao_conseguirei_realizar_a_contratacao() {
        
        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Clube meuClubeDoRepo = clubeRepo.buscarPorId(this.meuClube.getId());
        
        assertNotNull(jogadorDoRepo.getContratoId(), "O jogador deveria ter contrato ativo antes da tentativa.");
        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        
        assertFalse(meuClubeDoRepo.possuiJogadorId(jogadorDoRepo.getId()), "O jogador não deveria ter sido adicionado.");
        
        assertEquals(outroClube.getId(), jogadorDoRepo.getClubeId(), "O jogador deveria permanecer no clube original.");
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        
        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Clube meuClubeDoRepo = clubeRepo.buscarPorId(this.meuClube.getId());

        assertTrue(this.resultadoContratacao, "A contratação deveria ter sido bem-sucedida.");
        
        assertTrue(meuClubeDoRepo.possuiJogadorId(jogadorDoRepo.getId()), "O meu clube deveria ter o novo jogador.");
        
        assertEquals(meuClube.getId(), jogadorDoRepo.getClubeId(), "O clube do jogador deveria ser agora o meu clube.");
        
        assertNotNull(jogadorDoRepo.getContratoId(), "Um novo contrato deveria ter sido criado.");
        
        Contrato novoContrato = contratoRepo.buscarPorId(jogadorDoRepo.getContratoId());
        assertNotNull(novoContrato, "O novo contrato não foi encontrado no repositório.");
        assertEquals("ATIVO", novoContrato.getStatus(), "O contrato deveria estar ativo.");
        assertEquals(meuClube.getId(), novoContrato.getClubeId(), "O novo contrato deveria pertencer ao meu clube.");
    }
}
