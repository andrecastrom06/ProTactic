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
import dev.com.protactic.dominio.principal.cadastroAtleta.IClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.IJogadorRepository;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DefinicaoCapitaoFeature {

    private Jogador jogador;
    private List<Jogador> jogadores;
    private CapitaoService service;
    private CapitaoRepository repo;

    private IClubeRepository clubeRepo;
    private IJogadorRepository jogadorRepo;
    private IContratoRepository contratoRepo;
    
    private Clube clubeDoTeste; 

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Before
    public void init() {
        repo = new CapitaoMock();
        ((CapitaoMock) repo).limpar();
        
        clubeRepo = new ClubeMock();
        jogadorRepo = new JogadorMock();
        contratoRepo = new ContratoMock(); // Mock que criamos antes

        service = new CapitaoService(repo); 
        jogadores = new ArrayList<>();
    }

    //Cenários com um jogador

    @Dado("um jogador chamado {string}")
    public void criarJogador(String nome) {
        jogador = new Jogador(nome);
        // MUDANÇA: Salvar o jogador no mock para ele ganhar um ID
        jogadorRepo.salvar(jogador); 
        jogadores.clear();
        jogadores.add(jogador);
    }

    @E("ele possui contrato {string} com o {string}")
    public void setContrato(String status, String clubeNome) {
        
        clubeDoTeste = new Clube(clubeNome);
        clubeRepo.salvar(clubeDoTeste); // Agora o clubeDoTeste tem um ID

        Contrato contrato = new Contrato(clubeDoTeste.getId());
        contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
        contratoRepo.salvar(contrato); // Agora o contrato tem um ID

        // 3. Ligar os IDs ao jogador
        jogador.setClubeId(clubeDoTeste.getId());
        jogador.setContratoId(contrato.getId());
        jogador.setContratoAtivo("ativo".equalsIgnoreCase(status));
        
        // 4. Salvar o jogador atualizado
        jogadorRepo.salvar(jogador);
    }

    @E("ele chegou no dia {string} no clube")
    public void setDataChegada(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        jogador.setChegadaNoClube(data);
        jogadorRepo.salvar(jogador); // Salvar a mudança
    }

    @E("sua minutagem é {string}")
    public void setMinutagem(String minutagem) {
        jogador.setMinutagem(minutagem);
        jogadorRepo.salvar(jogador); // Salvar a mudança
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void definirCapitaoUnico(String nome) {
        service.definirCapitaoEntreJogadores(List.of(jogador));
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void verificaCapitao(String nome, String clube) {
        // MUDANÇA: Buscar o capitão pelo ID do clube, não pelo nome
        Jogador c = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        assertNotNull(c, "Capitão não foi salvo");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Nome do capitão não bate");

        //verificação de persistência
        Jogador persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getNome(), "O capitão persistido não corresponde ao esperado");
        
        // MUDANÇA: Verificar o ID do clube, não o nome
        assertEquals(clubeDoTeste.getId(), persistido.getClubeId(), "O capitão persistido está associado ao clube errado");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void verificaNaoCapitao(String nome, String clube) {
        // MUDANÇA: Buscar o capitão pelo ID do clube
        Jogador c = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        if (c != null) {
            assertFalse(c.getNome().equals(nome) && c.isCapitao(),
                    "Jogador '" + nome + "' não deveria ser capitão");
        } else {
            assertNull(c);
        }

        //persistência: nenhum capitão deve ter sido salvo
        assertNull(((CapitaoMock) repo).getUltimoCapitaoSalvo(),
                "Nenhum capitão deveria ter sido persistido");
    }

    // Cenários com dois ou mais jogadores (desempate)

    @Dado("dois jogadores {string} e {string}")
    public void criarJogadores(String n1, String n2) {
        jogadores.clear();
        Jogador j1 = new Jogador(n1);
        Jogador j2 = new Jogador(n2);
        // MUDANÇA: Salvar ambos para terem IDs
        jogadorRepo.salvar(j1);
        jogadorRepo.salvar(j2);
        jogadores.add(j1);
        jogadores.add(j2);
    }

    @E("ambos possuem contrato {string} com o {string}")
    public void setContratoTodos(String status, String clube) {
        // MUDANÇA: Criar UM clube e persistir
        clubeDoTeste = new Clube(clube);
        clubeRepo.salvar(clubeDoTeste); // clubeDoTeste tem ID

        for (Jogador j : jogadores) {
            // Criar um contrato NOVO para cada jogador
            Contrato contrato = new Contrato(clubeDoTeste.getId());
            contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
            contratoRepo.salvar(contrato); // contrato tem ID

            // Ligar IDs
            j.setClubeId(clubeDoTeste.getId());
            j.setContratoId(contrato.getId());
            j.setContratoAtivo("ativo".equalsIgnoreCase(status));
            
            jogadorRepo.salvar(j); // Salvar jogador
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
            jogadorRepo.salvar(j); // Salvar mudança
        }
    }

    @E("ambos chegaram no dia {string}")
    public void setDatasIguais(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        for (Jogador j : jogadores) {
            j.setChegadaNoClube(data);
            jogadorRepo.salvar(j); // Salvar mudança
        }
    }

    @Quando("o treinador tenta definir o capitão")
    public void definirCapitaoTodos() {
        service.definirCapitaoEntreJogadores(jogadores);
    }

    @Então("o treinador deve escolher manualmente quem será o capitão do {string}")
    public void escolhaManual(String clube) {
        // MUDANÇA: Buscar pelo ID
        Jogador c = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        assertNull(c, "Nenhum capitão deve ser definido em empate total");
        //persistência: nenhum capitão deve ter sido salvo
        assertNull(((CapitaoMock) repo).getUltimoCapitaoSalvo(),
                "Nenhum capitão deveria ter sido persistido no mock");
    }

    @Então("{string} deve ser definido como capitão do {string} por ter mais tempo de clube")
    public void verificaCapitaoTempo(String nome, String clube) {
        // MUDANÇA: Buscar pelo ID
        Jogador c = repo.buscarCapitaoPorClube(clubeDoTeste.getId()); 
        assertNotNull(c, "Capitão deveria ser definido");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Capitão com mais tempo não definido corretamente");
        
        //persistencia: verifica o capitão persistido
        Jogador persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getNome(), "O capitão persistido não corresponde ao esperado");
        
        // MUDANÇA: Verificar o ID do clube
        assertEquals(clubeDoTeste.getId(), persistido.getClubeId(), "O capitão persistido está associado ao clube errado");
    }
}