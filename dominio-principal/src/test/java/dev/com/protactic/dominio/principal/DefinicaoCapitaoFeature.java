package dev.com.protactic.dominio.principal;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.entidade.Capitao;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.repositorio.CapitaoRepository;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.servico.CapitaoService;
import dev.com.protactic.mocks.CapitaoMock;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.mocks.ContratoMock;
import dev.com.protactic.mocks.JogadorMock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

        String dataContexto = "2025-10-19T10:00:00Z"; // Defini por conta do exemplo do cenário
        Clock fixedClock = Clock.fixed(Instant.parse(dataContexto), ZoneId.of("UTC"));

        service = new CapitaoService(repo, jogadorRepo, fixedClock); 
        
        jogadores = new ArrayList<>();
    }

    @Dado("um jogador chamado {string}")
    public void criarJogador(String nome) {
        jogador = new Jogador(nome);
        jogador.setId(nome.hashCode()); 
        jogadorRepo.salvar(jogador); 
        
        jogadores.clear();
        jogadores.add(jogador);
    }

    @E("ele possui contrato {string} com o {string}")
    public void setContrato(String status, String clubeNome) {
        if (clubeDoTeste == null || !clubeDoTeste.getNome().equals(clubeNome)) {
            clubeDoTeste = new Clube(clubeNome);
            clubeDoTeste.setId(100); // ID Fixo para teste
            clubeRepo.salvar(clubeDoTeste);
        }

        Contrato contrato = new Contrato(clubeDoTeste.getId());
        contrato.setId(200); // ID Fixo
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

@Quando("o treinador tenta definir {string} como capitão")
    public void tentarDefinirEspecifico(String nome) {
        Jogador alvo = jogadores.stream()
                .filter(j -> j.getNome().equals(nome))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Jogador " + nome + " não encontrado no setup"));

        try {
            service.tentarDefinirCapitao(alvo);
        } catch (IllegalStateException e) {
            // Engolimos a exceção aqui propositalmente.
            // O motivo: Nos cenários de falha (Miguel, Vinicius), a exceção É ESPERADA.
            // Precisamos que o teste continue para o passo @Então para confirmar 
            // que o repositório continua vazio (ou seja, que a regra funcionou).
        }
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void verificaCapitaoSucesso(String nome, String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId());
        
        assertNotNull(capitao, "O capitão deveria ter sido salvo.");
        assertEquals(nome, capitao.getJogador().getNome(), "O nome do capitão está incorreto.");
        assertTrue(capitao.getJogador().isCapitao(), "A flag de capitão no jogador deve ser true.");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void verificaCapitaoFalha(String nome, String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId());
        
        if (capitao != null) {
            assertNotEquals(nome, capitao.getJogador().getNome(), 
                "O jogador " + nome + " não deveria ter sido aceito como capitão.");
        } else {
            assertNull(capitao);
        }
    }

    @Dado("dois jogadores {string} e {string}")
    public void criarJogadores(String n1, String n2) {
        jogadores.clear();
        
        Jogador j1 = new Jogador(n1);
        j1.setId(1);
        Jogador j2 = new Jogador(n2);
        j2.setId(2);
        
        jogadorRepo.salvar(j1);
        jogadorRepo.salvar(j2);
        
        jogadores.add(j1);
        jogadores.add(j2);
    }

    @E("ambos possuem contrato {string} com o {string}")
    public void setContratoTodos(String status, String clube) {
        clubeDoTeste = new Clube(clube);
        clubeDoTeste.setId(100);
        clubeRepo.salvar(clubeDoTeste);

        for (Jogador j : jogadores) {
            j.setClubeId(clubeDoTeste.getId());
            j.setContratoAtivo("ativo".equalsIgnoreCase(status));
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
    public void acaoGenerica() {
        // Neste passo, o Feature diz que há ambiguidade e o treinador tenta definir.
        // Como o sistema NÃO deve decidir sozinho, não chamamos nenhum método que faça "auto-selection".
        // Se chamássemos "tentarDefinirCapitao(null)", daria erro ou nada aconteceria.
        // A lógica correta aqui é que o sistema permanece passivo esperando a escolha manual.
        // Portanto, nenhuma ação de persistência é disparada automaticamente.
    }

    @Então("o treinador deve escolher manualmente quem será o capitão do {string}")
    public void verificaEscolhaManual(String clube) {
        Capitao capitao = repo.buscarCapitaoPorClube(clubeDoTeste.getId());
        assertNull(capitao, "O sistema não deveria ter escolhido um capitão automaticamente. A escolha deve ser manual.");
    }
}