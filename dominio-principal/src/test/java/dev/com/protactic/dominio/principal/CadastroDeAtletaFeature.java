package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.cadastroAtleta.*;
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

    private IJogadorRepository jogadorRepo;
    private IClubeRepository clubeRepo;
    // MUDANÇA: O serviço agora PRECISA do repositório de Contrato
    private IContratoRepository contratoRepo;

    @Before
    public void setup() {
        // MUDANÇA: Instanciar todos os mocks necessários
        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeMock();
        this.contratoRepo = new ContratoMock(); // Criamos este mock
        
        // MUDANÇA: O construtor do serviço agora recebe os 3 repositórios
        this.cadastroDeAtletaService = new CadastroDeAtletaService(jogadorRepo, clubeRepo, contratoRepo);

        // MUDANÇA: Os clubes precisam ser salvos para ganhar um ID
        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");
        this.clubeRepo.salvar(meuClube);
        this.clubeRepo.salvar(outroClube);
    }

    @Dado("que {string} com contrato ativo em outro clube existe")
    public void que_jogador_com_contrato_em_outro_clube_existe(String nomeJogador) {
        // MUDANÇA: A criação de dados de teste agora simula o mundo real (com IDs)
        
        // 1. Criar e salvar o Jogador para ele ter um ID
        this.jogador = new Jogador(nomeJogador);
        this.jogadorRepo.salvar(this.jogador); // Agora o jogador tem um ID (ex: 1)

        // 2. Criar e salvar o Contrato (usando o ID do outroClube)
        Contrato contratoExistente = new Contrato(outroClube.getId());
        this.contratoRepo.salvar(contratoExistente); // Agora o contrato tem um ID (ex: 1)

        // 3. Ligar os IDs no Agregado Jogador
        this.jogador.setContratoId(contratoExistente.getId());
        this.jogador.setClubeId(outroClube.getId());

        // 4. Adicionar o ID do jogador no Agregado Clube
        this.outroClube.adicionarJogadorId(this.jogador.getId());
        
        // 5. Salvar as mudanças nos repositórios
        this.jogadorRepo.salvar(this.jogador);
        this.clubeRepo.salvar(this.outroClube);
    }

    @Dado("que {string} sem contrato existe")
    public void que_jogador_sem_contrato_existe(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);
        // MUDANÇA: Mesmo sem contrato, o jogador precisa ser salvo para ter um ID
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
        // MUDANÇA: As asserções agora buscam dos repositórios e verificam IDs
        
        // 1. Pega os dados "frescos" do mock (simulando o banco)
        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Clube meuClubeDoRepo = clubeRepo.buscarPorId(this.meuClube.getId());
        
        assertNotNull(jogadorDoRepo.getContratoId(), "O jogador deveria ter contrato ativo antes da tentativa.");
        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        
        // 2. Verifica se o ID do jogador NÃO está na lista de IDs do clube
        assertFalse(meuClubeDoRepo.possuiJogadorId(jogadorDoRepo.getId()), "O jogador não deveria ter sido adicionado.");
        
        // 3. Verifica se o ID do clube no jogador ainda é o do clube antigo
        assertEquals(outroClube.getId(), jogadorDoRepo.getClubeId(), "O jogador deveria permanecer no clube original.");
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        // MUDANÇA: As asserções agora buscam dos repositórios e verificam IDs
        
        // 1. Pega os dados "frescos" do mock (simulando o banco)
        Jogador jogadorDoRepo = jogadorRepo.buscarPorId(this.jogador.getId());
        Clube meuClubeDoRepo = clubeRepo.buscarPorId(this.meuClube.getId());

        assertTrue(this.resultadoContratacao, "A contratação deveria ter sido bem-sucedida.");
        
        // 2. Verifica se o ID do jogador ESTÁ na lista de IDs do clube
        assertTrue(meuClubeDoRepo.possuiJogadorId(jogadorDoRepo.getId()), "O meu clube deveria ter o novo jogador.");
        
        // 3. Verifica se o ID do clube no jogador agora é o ID do meuClube
        assertEquals(meuClube.getId(), jogadorDoRepo.getClubeId(), "O clube do jogador deveria ser agora o meu clube.");
        
        // 4. Verifica se o jogador tem um ID de contrato
        assertNotNull(jogadorDoRepo.getContratoId(), "Um novo contrato deveria ter sido criado.");
        
        // 5. Busca o NOVO contrato e verifica seu status
        Contrato novoContrato = contratoRepo.buscarPorId(jogadorDoRepo.getContratoId());
        assertNotNull(novoContrato, "O novo contrato não foi encontrado no repositório.");
        assertEquals("ATIVO", novoContrato.getStatus(), "O contrato deveria estar ativo.");
        assertEquals(meuClube.getId(), novoContrato.getClubeId(), "O novo contrato deveria pertencer ao meu clube.");
    }
}
