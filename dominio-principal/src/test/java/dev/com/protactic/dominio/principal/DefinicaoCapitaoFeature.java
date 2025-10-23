package dev.com.protactic.dominio.principal;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.capitao.CapitaoService;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;
import dev.com.protactic.mocks.CapitaoMock;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.mocks.JogadorMock;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DefinicaoCapitaoFeature {

    private Jogador jogador;
    private List<Jogador> jogadores;
    private CapitaoService service;
    private CapitaoRepository repo;

    private ClubeRepository clubeRepo;
    private JogadorRepository jogadorRepo;
    private ContratoRepository contratoRepo;
    
    private Clube clubeDoTeste; 

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Before
    public void init() {
        repo = new CapitaoMock();
        ((CapitaoMock) repo).limpar();
        
        clubeRepo = new ClubeMock();
        jogadorRepo = new JogadorMock();
        contratoRepo = new ContratoMock();

        service = new CapitaoService(repo); 
        jogadores = new ArrayList<>();
    }

    @Dado("um jogador chamado {string}")
    public void criarJogador(String nome) {
        jogador = new Jogador(nome);
        jogadorRepo.salvar(jogador); 
        jogadores.clear();
        jogadores.add(jogador);
    }

    @E("ele possui contrato {string} com o {string}")
    public void setContrato(String status, String clubeNome) {
        clubeDoTeste = new Clube(clubeNome);
        clubeRepo.salvar(clubeDoTeste);

        Contrato contrato = new Contrato(clubeDoTeste.getId());
        contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
        contratoRepo.salvar(contrato);

        jogador.setClubeId(clubeDoTeste.getId());
        jogador.setContratoId(contrato.getId());
        jogador.setContratoAtivo("ativo".equalsIgnoreCase(status));
        
        jogadorRepo.salvar(jogador);
    }

    @E("ele chegou no dia {string} no clube")
    public void setDataChegada(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        jogador.setChegadaNoClube(data);
        jogadorRepo.salvar(jogador);
    }

    @E("sua minutagem é {string}")
    public void setMinutagem(String minutagem) {
        jogador.setMinutagem(minutagem);
        jogadorRepo.salvar(jogador);
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void definirCapitaoUnico(String nome) {
        service.definirCapitaoEntreJogadores(List.of(jogador));
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void verificaCapitao(String nome, String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId());
        assertNotNull(capitao, "Capitão não foi salvo");

        Jogador c = capitao.getJogador();
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Nome do capitão não bate");

        Capitao persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getJogador().getNome(), "O capitão persistido não corresponde ao esperado");
        
        assertEquals(clubeDoTeste.getId(), persistido.getClubeId(), "O capitão persistido está associado ao clube errado");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void verificaNaoCapitao(String nome, String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        if (capitao != null) {
            Jogador c = capitao.getJogador();
            assertFalse(c.getNome().equals(nome) && c.isCapitao(),
                    "Jogador '" + nome + "' não deveria ser capitão");
        } else {
            assertNull(capitao);
        }

        assertNull(((CapitaoMock) repo).getUltimoCapitaoSalvo(),
                "Nenhum capitão deveria ter sido persistido");
    }

    @Dado("dois jogadores {string} e {string}")
    public void criarJogadores(String n1, String n2) {
        jogadores.clear();
        Jogador j1 = new Jogador(n1);
        Jogador j2 = new Jogador(n2);
        jogadorRepo.salvar(j1);
        jogadorRepo.salvar(j2);
        jogadores.add(j1);
        jogadores.add(j2);
    }

    @E("ambos possuem contrato {string} com o {string}")
    public void setContratoTodos(String status, String clube) {
        clubeDoTeste = new Clube(clube);
        clubeRepo.salvar(clubeDoTeste);

        for (Jogador j : jogadores) {
            Contrato contrato = new Contrato(clubeDoTeste.getId());
            contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
            contratoRepo.salvar(contrato);

            j.setClubeId(clubeDoTeste.getId());
            j.setContratoId(contrato.getId());
            j.setContratoAtivo("ativo".equalsIgnoreCase(status));
            
            jogadorRepo.salvar(j);
        }
    }

    @E("ambos têm minutagem {string}")
    public void setMinutagemTodos(String minutagem) {
        for (Jogador j : jogadores) {
            j.setMinutagem(minutagem);
            jogadorRepo.salvar(j);
        }
    }

    @E("{string} chegou no dia {string} e {string} chegou no dia {string}")
    public void setDatasDiferentes(String n1, String d1, String n2, String d2) {
        LocalDate data1 = LocalDate.parse(d1, DF);
        LocalDate data2 = LocalDate.parse(d2, DF);
        for (Jogador j : jogadores) {
            if (j.getNome().equals(n1)) {
                j.setChegadaNoClube(data1);
            } else if (j.getNome().equals(n2)) {
                j.setChegadaNoClube(data2);
            }
            jogadorRepo.salvar(j);
        }
    }

    @E("ambos chegaram no dia {string}")
    public void setDatasIguais(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        for (Jogador j : jogadores) {
            j.setChegadaNoClube(data);
            jogadorRepo.salvar(j);
        }
    }

    @Quando("o treinador tenta definir o capitão")
    public void definirCapitaoTodos() {
        service.definirCapitaoEntreJogadores(jogadores);
    }

    @Então("o treinador deve escolher manualmente quem será o capitão do {string}")
    public void escolhaManual(String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        assertNull(capitao, "Nenhum capitão deve ser definido em empate total");
        assertNull(((CapitaoMock) repo).getUltimoCapitaoSalvo(),
                "Nenhum capitão deveria ter sido persistido no mock");
    }

    @Então("{string} deve ser definido como capitão do {string} por ter mais tempo de clube")
    public void verificaCapitaoTempo(String nome, String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        assertNotNull(capitao, "Capitão deveria ser definido");

        Jogador c = capitao.getJogador();
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Capitão com mais tempo não definido corretamente");
        
        Capitao persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getJogador().getNome(), "O capitão persistido não corresponde ao esperado");
        
        assertEquals(clubeDoTeste.getId(), persistido.getClubeId(), "O capitão persistido está associado ao clube errado");
    }
}
