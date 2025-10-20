package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.cadastroAtleta.*;
import dev.com.protactic.mocks.ClubeMock;
import dev.com.protactic.mocks.JogadorMock;
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

    @Before
    public void setup() {
        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");

        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeMock();
        this.cadastroDeAtletaService = new CadastroDeAtletaService(jogadorRepo, clubeRepo);
    }

    @Dado("que {string} com contrato ativo em outro clube existe")
    public void que_jogador_com_contrato_em_outro_clube_existe(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);

        Contrato contratoExistente = new Contrato(outroClube);
        this.jogador.setContrato(contratoExistente);

        outroClube.adicionarJogador(this.jogador);
    }

    @Dado("que {string} sem contrato existe")
    public void que_jogador_sem_contrato_existe(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);
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
        assertNotNull(jogador.getContrato(), "O jogador deveria ter contrato ativo antes da tentativa.");
        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        assertFalse(meuClube.possuiJogador(jogador.getNome()), "O jogador não deveria ter sido adicionado.");
        assertEquals(outroClube, jogador.getClube(), "O jogador deveria permanecer no clube original.");

        // 🔎 Validação no repositório
        assertNull(jogadorRepo.buscarPorNome(jogador.getNome()), 
            "O jogador não deveria ter sido salvo no repositório.");
        Clube clubePersistido = clubeRepo.buscarPorNome(meuClube.getNome());
        if (clubePersistido != null) {
            assertFalse(clubePersistido.possuiJogador(jogador.getNome()), 
                "O jogador não deveria aparecer no clube persistido.");
        }
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        assertTrue(this.resultadoContratacao, "A contratação deveria ter sido bem-sucedida.");
        assertTrue(meuClube.possuiJogador(jogador.getNome()), "O meu clube deveria ter o novo jogador.");
        assertEquals(meuClube, jogador.getClube(), "O clube do jogador deveria ser agora o meu clube.");
        assertNotNull(jogador.getContrato(), "Um novo contrato deveria ter sido criado.");
        assertEquals("ATIVO", jogador.getContrato().getStatus(), "O contrato deveria estar ativo.");

        // 🔎 Validação no repositório
        Jogador jogadorPersistido = jogadorRepo.buscarPorNome(jogador.getNome());
        assertNotNull(jogadorPersistido, "O jogador deveria ter sido salvo no repositório.");
        assertEquals(meuClube, jogadorPersistido.getClube(), 
            "O clube persistido do jogador deveria ser o meu clube.");

        Clube clubePersistido = clubeRepo.buscarPorNome(meuClube.getNome());
        assertNotNull(clubePersistido, "O clube deveria ter sido salvo no repositório.");
        assertTrue(clubePersistido.possuiJogador(jogador.getNome()), 
            "O clube persistido deveria conter o jogador.");
    }
}
