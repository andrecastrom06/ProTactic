package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import dev.com.protactic.dominio.principal.cadastroAtleta.IClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;
import dev.com.protactic.mocks.PropostaMock;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.mocks.JogadorMock;
import io.cucumber.java.Before; 
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class PropostaContratacaoFeature {

    private Jogador jogador;
    private Clube clubeProponente;
    private Date dataAtual;
    private Proposta proposta;
    private Exception excecao;

    private PropostaMock propostaMock; 
    private IContratoRepository contratoRepo;
    private IJogadorRepository jogadorRepo;
    private IClubeRepository clubeRepo;
    
    private PropostaService propostaService;
    
    private Map<String, Clube> clubesDoTeste;

    @Before
    public void setup() {
        this.propostaMock = new PropostaMock();
        this.contratoRepo = new ContratoMock();
        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeMock();

        this.propostaService = new PropostaService(propostaMock, contratoRepo);
        
        this.clubesDoTeste = new HashMap<>();

        this.jogador = null;
        this.clubeProponente = null;
        this.dataAtual = null;
        this.proposta = null;
        this.excecao = null;

        ContratoMock.limparMock();
    }
    
    private Clube findOrCreateClube(String nomeClube) {
        if (clubesDoTeste.containsKey(nomeClube)) {
            return clubesDoTeste.get(nomeClube);
        }
        Clube novoClube = new Clube(nomeClube);
        this.clubeRepo.salvar(novoClube);
        clubesDoTeste.put(nomeClube, novoClube);
        return novoClube;
    }


    @Dado("um jogador chamado {string} que não tem contrato")
    public void um_jogador_chamado_que_não_tem_contrato(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);
        this.jogadorRepo.salvar(this.jogador);
    }

    @Dado("um jogador chamado {string} que tem contrato com o {string}")
    public void um_jogador_chamado_que_tem_contrato_com_o(String nomeJogador, String clube) {
        
        Clube clubeObj = findOrCreateClube(clube);

        Contrato contrato = new Contrato(clubeObj.getId());
        contrato.setStatus("ATIVO");
        this.contratoRepo.salvar(contrato); 

        this.jogador = new Jogador(nomeJogador);
        this.jogadorRepo.salvar(this.jogador); 

        this.jogador.setContratoId(contrato.getId());
        this.jogador.setClubeId(clubeObj.getId());
        this.jogadorRepo.salvar(this.jogador);
    }

    @Dado("a data é {string}")
    public void a_data_e(String data) throws ParseException {
        this.dataAtual = new SimpleDateFormat("dd/MM/yyyy").parse(data);
    }

    @Quando("um analista do {string} cria uma proposta de contrato para {string}")
    public void um_analista_do_cria_uma_proposta_de_contrato_para(String nomeClube, String nomeJogador) {
        
        this.clubeProponente = findOrCreateClube(nomeClube);

        try {
            this.proposta = propostaService.criarProposta(jogador, clubeProponente, dataAtual);
        } catch (Exception e) {
            this.excecao = e;
        }
    }

    @Então("a proposta deve ser registrada com sucesso")
    public void a_proposta_deve_ser_registrada_com_sucesso() {
        assertNotNull(proposta, "A proposta não deveria ser nula");
        
        assertEquals(jogador.getId(), proposta.getJogadorId(), "Jogador ID incorreto na proposta");
        assertEquals(clubeProponente.getId(), proposta.getPropositorId(), "Clube propositor ID incorreto");
        assertEquals(dataAtual, proposta.getData(), "Data incorreta na proposta");

        Proposta persistida = propostaMock.getUltimaProposta();
        assertNotNull(persistida, "A proposta deveria ter sido salva no repositório");
        assertEquals(proposta.getId(), persistida.getId(), "A proposta persistida não corresponde à criada");
    }

    @Então("o sistema deve lançar uma exceção com a mensagem {string}")
    public void o_sistema_deve_lançar_uma_exceção_com_a_mensagem(String mensagemEsperada) {
        assertNotNull(excecao, "Uma exceção era esperada, mas não foi lançada.");
        
        assertEquals(mensagemEsperada, excecao.getMessage(), "A mensagem de erro não foi a esperada.");

        assertNull(propostaMock.getUltimaProposta(), "Nenhuma proposta deveria ter sido salva no repositório");
    }
}
