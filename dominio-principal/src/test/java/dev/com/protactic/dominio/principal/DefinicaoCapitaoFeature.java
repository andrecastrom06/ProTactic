package dev.com.protactic.dominio.principal;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.capitao.CapitaoService;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;
import dev.com.protactic.mocks.CapitaoMock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DefinicaoCapitaoFeature {

    private Jogador jogador;
    private List<Jogador> jogadores;
    private CapitaoService service;
    private CapitaoRepository repo;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Before
    public void init() {
        repo = new CapitaoMock();
        ((CapitaoMock) repo).limpar();
        service = new CapitaoService(repo);
        jogadores = new ArrayList<>();
    }

    //Cenários com um jogador

    @Dado("um jogador chamado {string}")
    public void criarJogador(String nome) {
        jogador = new Jogador(nome);
        jogadores.clear();
        jogadores.add(jogador);
    }

    @E("ele possui contrato {string} com o {string}")
    public void setContrato(String status, String clubeNome) {
        Clube clube = new Clube(clubeNome);
        Contrato contrato = new Contrato(clube);
        contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
        jogador.setClube(clube);
        jogador.setContrato(contrato);
        jogador.setContratoAtivo("ativo".equalsIgnoreCase(status));
    }

    @E("ele chegou no dia {string} no clube")
    public void setDataChegada(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        jogador.setChegadaNoClube(data);
    }

    @E("sua minutagem é {string}")
    public void setMinutagem(String minutagem) {
        jogador.setMinutagem(minutagem);
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void definirCapitaoUnico(String nome) {
        service.definirCapitaoEntreJogadores(List.of(jogador));
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void verificaCapitao(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNotNull(c, "Capitão não foi salvo");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Nome do capitão não bate");

        //verificação de persistência
        Jogador persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getNome(), "O capitão persistido não corresponde ao esperado");
        assertEquals(clube, persistido.getClube().getNome(), "O capitão persistido está associado ao clube errado");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void verificaNaoCapitao(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
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
        jogadores.add(new Jogador(n1));
        jogadores.add(new Jogador(n2));
    }

    @E("ambos possuem contrato {string} com o {string}")
    public void setContratoTodos(String status, String clube) {
        for (Jogador j : jogadores) {
            Clube c = new Clube(clube);
            Contrato contrato = new Contrato(c);
            contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
            j.setClube(c);
            j.setContrato(contrato);
            j.setContratoAtivo("ativo".equalsIgnoreCase(status));
        }
    }

    @E("ambos têm minutagem {string}")
    public void setMinutagemTodos(String minutagem) {
        for (Jogador j : jogadores) {
            j.setMinutagem(minutagem);
        }
    }

    @E("{string} chegou no dia {string} e {string} chegou no dia {string}")
    public void setDatasDiferentes(String n1, String d1, String n2, String d2) {
        LocalDate data1 = LocalDate.parse(d1, DF);
        LocalDate data2 = LocalDate.parse(d2, DF);
        for (Jogador j : jogadores) {
            if (j.getNome().equals(n1)) j.setChegadaNoClube(data1);
            else if (j.getNome().equals(n2)) j.setChegadaNoClube(data2);
        }
    }

    @E("ambos chegaram no dia {string}")
    public void setDatasIguais(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, DF);
        for (Jogador j : jogadores) j.setChegadaNoClube(data);
    }

    @Quando("o treinador tenta definir o capitão")
    public void definirCapitaoTodos() {
        service.definirCapitaoEntreJogadores(jogadores);
    }

    @Então("o treinador deve escolher manualmente quem será o capitão do {string}")
    public void escolhaManual(String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNull(c, "Nenhum capitão deve ser definido em empate total");
        //persistência: nenhum capitão deve ter sido salvo
        assertNull(((CapitaoMock) repo).getUltimoCapitaoSalvo(),
                "Nenhum capitão deveria ter sido persistido no mock");
    }

    @Então("{string} deve ser definido como capitão do {string} por ter mais tempo de clube")
    public void verificaCapitaoTempo(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNotNull(c, "Capitão deveria ser definido");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Capitão com mais tempo não definido corretamente");
        //persistencia: verifica o capitão persistido
        Jogador persistido = ((CapitaoMock) repo).getUltimoCapitaoSalvo();
        assertNotNull(persistido, "O capitão não foi persistido no mock");
        assertEquals(nome, persistido.getNome(), "O capitão persistido não corresponde ao esperado");
        assertEquals(clube, persistido.getClube().getNome(), "O capitão persistido está associado ao clube errado");
    }
}
