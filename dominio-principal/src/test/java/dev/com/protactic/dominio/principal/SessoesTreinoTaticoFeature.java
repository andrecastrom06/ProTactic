package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreino;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoRepositoryFake;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessoesTreinoTaticoFeature {

    private SessaoTreinoRepositoryFake repo;
    private SessaoTreinoService service;
    private SessaoTreino sessaoCriada;
    private Partida partidaAtual;
    private Jogador jogadorAtual;
    private List<Jogador> jogadores = new ArrayList<>();
    private Exception excecao;

    @Dado("que o treinador está na área de planejamento de treinos")
    public void que_o_treinador_está_na_área_de_planejamento_de_treinos() {
        repo = new SessaoTreinoRepositoryFake();
        service = new SessaoTreinoService(repo);
    }

    @Dado("que existe o jogo {string} no calendário")
    public void que_existe_o_jogo_no_calendário(String nomeJogo) {
        String[] clubes = nomeJogo.split(" vs ");
        partidaAtual = new Partida(
                1,
                new Clube(clubes[0].trim()),
                new Clube(clubes[1].trim()),
                new Date(),
                "19:00",
                0,
                0
        );
    }

    @Dado("que o jogador {string} tem o status {string}")
    public void que_o_jogador_tem_o_status(String nomeJogador, String status) {
        jogadorAtual = new Jogador(nomeJogador);
        jogadorAtual.setStatus(status);
        jogadores.add(jogadorAtual);
    }

    @Dado("que já existe a sessão de treino {string} para o jogo {string}")
    public void que_já_existe_a_sessão_de_treino_para_o_jogo(String nomeSessao, String nomeJogo) {
        SessaoTreino sessaoExistente = new SessaoTreino(nomeSessao, partidaAtual);
        repo.salvar(sessaoExistente);
    }

    @Dado("que não existem jogos no calendário")
    public void que_não_existem_jogos_no_calendário() {
        partidaAtual = null;
    }

    @Quando("o treinador cria a sessão de treino {string} para o jogo {string}")
    public void o_treinador_cria_a_sessão_de_treino_para_o_jogo(String nomeSessao, String nomeJogo) {
        try {
            sessaoCriada = service.criarSessao(nomeSessao, partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("o treinador cria uma sessão de treino para o jogo {string}")
    public void o_treinador_cria_uma_sessão_de_treino_para_o_jogo(String nomeJogo) {
        try {
            sessaoCriada = service.criarSessao("SessaoAuto", partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("o treinador tenta criar uma sessão de treino tático")
    public void o_treinador_tenta_criar_uma_sessão_de_treino_tático() {
        try {
            sessaoCriada = service.criarSessao("TreinoTeste", partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Então("o sistema deve permitir a criação da sessão")
    public void o_sistema_deve_permitir_a_criação_da_sessão() {
        assertNotNull(sessaoCriada);

        //verifica se a sessão está realmente no repositório
        List<SessaoTreino> sessoes = repo.listarPorPartida(partidaAtual.getDescricao());
        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(sessaoCriada.getNome())));
    }

    @Então("o sistema deve impedir a criação da sessão")
    public void o_sistema_deve_impedir_a_criação_da_sessão() {
        assertNull(sessaoCriada);
        assertNotNull(excecao);
    }

    @Então("o jogador {string} deve aparecer na lista de convocação")
    public void o_jogador_deve_aparecer_na_lista_de_convocação(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .anyMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)));
    }

    @Então("o jogador {string} não deve aparecer na lista de convocação")
    public void o_jogador_não_deve_aparecer_na_lista_de_convocação(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .noneMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)));
    }

    @Então("a sessão de treino {string} deve estar vinculada ao jogo {string}")
    public void a_sessão_de_treino_deve_estar_vinculada_ao_jogo(String nomeSessao, String nomeJogo) {
        List<SessaoTreino> sessoes = repo.listarPorPartida(nomeJogo);

        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(nomeSessao)
                        && s.getPartida().getDescricao().equalsIgnoreCase(nomeJogo)));
    }

    @Então("o sistema deve exibir a mensagem {string}")
    public void o_sistema_deve_exibir_a_mensagem(String mensagemEsperada) {
        assertNotNull(excecao);
        assertEquals(mensagemEsperada, excecao.getMessage());
    }
}
