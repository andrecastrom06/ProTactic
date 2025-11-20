package dev.com.protactic.dominio.principal;

// ... (todos os imports)
import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.EscalacaoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.SuspensaoRepository;

import dev.com.protactic.mocks.EscalacaoMock;
import dev.com.protactic.mocks.JogadorMock;
import dev.com.protactic.mocks.RegistroLesoesMock;
import dev.com.protactic.mocks.RegistroCartoesMock;
import dev.com.protactic.mocks.SuspensaoMock;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.cucumber.java.Before;
import static org.junit.jupiter.api.Assertions.*;

public class DefinirEsquemaTaticoFeature {

    private EscalacaoRepository repository;
    private JogadorRepository jogadorRepo;
    private RegistroLesoesRepository lesoesRepo;
    private RegistroCartoesRepository cartoesRepo;
    private SuspensaoRepository suspensaoRepo;
    private RegistroCartoesService cartoesService;
    private DefinirEsquemaTaticoService service;
    
    private String jogoEmContexto;
    private String ultimoJogadorSalvo;
    private Exception excecaoOcorrida;
    private final int CLUBE_ID_TESTE = 1; // ID fixo para testes

    @Before
    public void setup() {
        repository = new EscalacaoMock();
        jogadorRepo = new JogadorMock();
        lesoesRepo = new RegistroLesoesMock(); 
        cartoesRepo = new RegistroCartoesMock();
        suspensaoRepo = new SuspensaoMock(); 

        cartoesService = new RegistroCartoesService(cartoesRepo, suspensaoRepo);

        service = new DefinirEsquemaTaticoService(
            repository,
            jogadorRepo,
            lesoesRepo,
            cartoesService
        );
        
        excecaoOcorrida = null;
    }

    @Dado("que o treinador está na tela de gerenciamento de escalação e tática")
    public void que_o_treinador_esta_na_tela_de_gerenciamento_de_escalacao_e_tatica() {
    }

    @Dado("que existe um jogo marcado para {string}")
    public void que_existe_um_jogo_marcado_para(String data) {
        jogoEmContexto = data;
    }

    @Quando("o treinador cadastrar a escalação")
    public void o_treinador_cadastrar_a_escalacao() {
        repository.salvarJogadorNaEscalacao(jogoEmContexto, "ESCALAÇÃO_VAZIA", CLUBE_ID_TESTE);
    }

    @Então("a escalação aparecerá vinculada ao jogo do dia {string}")
    public void a_escalacao_aparecera_vinculada_ao_jogo_do_dia(String data) {
        var escalacaoPersistida = repository.obterEscalacaoPorData(data, CLUBE_ID_TESTE);
        assertNotNull(escalacaoPersistida, "Nenhuma escalação encontrada para o jogo.");
        assertFalse(escalacaoPersistida.isEmpty(), "Escalação não foi registrada para o jogo.");
    }

    private Jogador garantirJogadorExiste(String nomeJogador) {
        Jogador j = jogadorRepo.buscarPorNome(nomeJogador);
        if (j == null) {
            j = new Jogador(nomeJogador);
            jogadorRepo.salvar(j);
        }
        lesoesRepo.cadastrarAtleta(nomeJogador);
        return j;
    }

    @E("o jogador {string} tem uma lesão de grau {int}")
    public void o_jogador_tem_uma_lesao_de_grau(String nomeJogador, Integer grau) {
        garantirJogadorExiste(nomeJogador);
        if (grau >= 0) {
            lesoesRepo.salvarLesaoAtiva(nomeJogador, grau);
        } else {
            lesoesRepo.encerrarLesaoAtiva(nomeJogador);
        }
    }

    @E("{string} possui contrato ativo e não está suspenso")
    public void possui_contrato_ativo_e_nao_esta_suspenso(String nomeJogador) {
        garantirJogadorExiste(nomeJogador);
        lesoesRepo.definirContratoAtivo(nomeJogador, true);
        Suspensao s = suspensaoRepo.buscarPorAtleta(nomeJogador).orElse(null);
        if (s != null) {
            s.setSuspenso(false);
            suspensaoRepo.salvarOuAtualizar(s);
        } else {
            Suspensao nova = new Suspensao(0, nomeJogador, false, 0, 0);
            suspensaoRepo.salvarOuAtualizar(nova);
        }
    }

    @E("o jogador {string} está suspenso")
    public void o_jogador_esta_suspenso(String nomeJogador) {
        garantirJogadorExiste(nomeJogador);
        cartoesRepo.salvarCartao(new RegistroCartao(nomeJogador, "amarelo"));
        cartoesRepo.salvarCartao(new RegistroCartao(nomeJogador, "amarelo"));
        cartoesRepo.salvarCartao(new RegistroCartao(nomeJogador, "amarelo"));
        
        cartoesService.verificarSuspensao(nomeJogador);
    }
  

    @E("{string} possui contrato ativo e não está lesionado")
    public void possui_contrato_ativo_e_nao_esta_lesionado(String nomeJogador) {
        garantirJogadorExiste(nomeJogador);
        lesoesRepo.definirContratoAtivo(nomeJogador, true);
        lesoesRepo.encerrarLesaoAtiva(nomeJogador); 
    }

    @Quando("o treinador cadastrar a escalação incluindo {string}")
    public void o_treinador_cadastrar_a_escalacao_incluindo(String nomeJogador) {
        this.ultimoJogadorSalvo = nomeJogador;
        try {
            service.registrarJogadorEmEscalacao(
                    jogoEmContexto,
                    nomeJogador,
                    CLUBE_ID_TESTE
            );
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Então("a escalação será registrada com sucesso")
    public void a_escalacao_sera_registrada_com_sucesso() {
        assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção");
        var escalacaoPersistida = repository.obterEscalacaoPorData(jogoEmContexto, CLUBE_ID_TESTE);

        assertNotNull(escalacaoPersistida, "Nenhuma escalação foi encontrada para o jogo.");
        assertFalse(escalacaoPersistida.isEmpty(), "A escalação deveria ter sido registrada.");

        boolean contemJogador = escalacaoPersistida.stream()
                .anyMatch(jogador -> jogador.equals(ultimoJogadorSalvo));

        assertTrue(contemJogador, "O jogador '" + ultimoJogadorSalvo + "' não foi persistido na escalação.");
    }

    @Então("a escalação não poderá ser registrada")
    public void a_escalacao_nao_podera_ser_registrada() {
        var escalacaoPersistida = repository.obterEscalacaoPorData(jogoEmContexto, CLUBE_ID_TESTE);

        if (escalacaoPersistida != null) {
             boolean contemJogador = escalacaoPersistida.stream()
                .anyMatch(jogador -> jogador.equals(ultimoJogadorSalvo));
             assertFalse(contemJogador, "O jogador '" + ultimoJogadorSalvo + "' foi escalado indevidamente.");
        } else {
            assertTrue(true, "Escalação não foi registrada, como esperado.");
        }
    }
}