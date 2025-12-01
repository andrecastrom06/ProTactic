package dev.com.protactic.dominio.principal;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.mocks.JogadorMock;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.DispensaService;

public class DispensaJogadorFeature {

    private Jogador jogador;
    private Clube clube;
    private String mensagemErro;
    
    private ContratoRepository contratoRepo;
    private JogadorRepository jogadorRepo;
    private ClubeRepository clubeRepo;
    private DispensaService dispensaService;

    private Integer contratoId;

    @Before
    public void setup() {
        this.contratoRepo = new ContratoMock();
        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeMock();
        
        this.dispensaService = new DispensaService(contratoRepo, jogadorRepo, clubeRepo);

        
        ContratoMock.limparMock(); 
        
        this.jogador = null;
        this.clube = null;
        this.contratoId = null;
        this.mensagemErro = null;
    }


    @Dado("um jogador chamado {string} com contrato ativo com o {string}")
    public void um_jogador_chamado_com_contrato_ativo_com_o(String nome, String nomeClube) {
        this.clube = new Clube(nomeClube);
        this.clubeRepo.salvar(this.clube);

        Contrato contrato = new Contrato(this.clube.getId());
        contrato.setStatus("ATIVO");
        this.contratoRepo.salvar(contrato);
        this.contratoId = contrato.getId();

        this.jogador = new Jogador(nome, this.clube.getId());
        this.jogador.setContratoId(this.contratoId);
        this.jogadorRepo.salvar(this.jogador);
        
        this.clube.adicionarJogadorId(this.jogador.getId());
        this.clubeRepo.salvar(this.clube);
    }

    @E("o jogador está saudável")
    public void o_jogador_esta_saudavel() {
        jogador.setSaudavel(true);
        jogadorRepo.salvar(jogador);
    }

    @E("o jogador está machucado")
    public void o_jogador_esta_machucado() {
        jogador.setSaudavel(false);
        jogadorRepo.salvar(jogador);
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
        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Contrato contratoDoRepo = contratoRepo.buscarPorId(this.contratoId);
        Clube clubeDoRepo = clubeRepo.buscarPorId(this.clube.getId());

        if ("Passes Livres".equalsIgnoreCase(esperado)) {
            assertNull(jogadorDoRepo.getClubeId(), "Jogador dispensado deveria ter clubeId nulo");
            assertNull(jogadorDoRepo.getContratoId(), "Jogador dispensado deveria ter contratoId nulo");
        } else {
            assertEquals(this.clube.getId(), jogadorDoRepo.getClubeId());
        }

        assertNotNull(contratoDoRepo, "O contrato deveria ter sido encontrado no repositório");
        assertEquals("RESCINDIDO", contratoDoRepo.getStatus(), "O contrato persistido deveria estar rescindido");
        
        assertNotNull(clubeDoRepo, "O clube deveria ter sido encontrado no repositório");
        assertFalse(clubeDoRepo.possuiJogadorId(this.jogador.getId()), "O clube não deveria mais possuir o ID do jogador");
    }

    @Então("o sistema deve bloquear a rescisão com a mensagem {string}")
    public void o_sistema_deve_bloquear_a_rescisao_com_a_mensagem(String mensagemEsperada) {
        assertEquals(mensagemEsperada, this.mensagemErro, "Mensagem de erro incorreta");

        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Contrato contratoDoRepo = contratoRepo.buscarPorId(this.contratoId);
        Clube clubeDoRepo = clubeRepo.buscarPorId(this.clube.getId());

        assertNotNull(contratoDoRepo, "O contrato não foi encontrado");
        assertEquals("ATIVO", contratoDoRepo.getStatus(), "O contrato deveria permanecer ATIVO");
        
        assertNotNull(jogadorDoRepo, "O jogador não foi encontrado");
        assertEquals(this.clube.getId(), jogadorDoRepo.getClubeId(), "O jogador deveria continuar no clube");
        
        assertNotNull(clubeDoRepo, "O clube não foi encontrado");
        assertTrue(clubeDoRepo.possuiJogadorId(this.jogador.getId()), "O clube deveria continuar com o jogador");
    }
}
