package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreino;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;
import dev.com.protactic.mocks.SessaoTreinoMock;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessoesTreinoTaticoFeature {

    private SessaoTreinoMock repo = new SessaoTreinoMock();
    private SessaoTreinoService service = new SessaoTreinoService(repo);
    private List<Jogador> jogadores = new ArrayList<>();

    private SessaoTreino sessaoCriada;
    private Partida partidaAtual;
    private Jogador jogadorAtual;
    private Exception excecao;

    @Dado("que o treinador está na área de planejamento de treinos")

    // Inicializa o estado antes de cada cenário(inicia testes com tudo limpo)
    public void iniciarPlanejamento() {
        sessaoCriada = null;
        excecao = null;
        jogadores.clear();
        partidaAtual = null;
        jogadorAtual = null;
    }

    @Dado("que existe o jogo {string} no calendário")
    //Cria uma partida com os clubes extraídos do nome do jogo
    public void criarPartida(String nomeJogo) {
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
    //Cria um jogador com o nome e status fornecidos e o adiciona à lista de jogadores
    public void criarJogador(String nomeJogador, String status) {
        jogadorAtual = new Jogador(nomeJogador);
        jogadorAtual.setStatus(status);
        jogadores.add(jogadorAtual);
    }

    @Dado("que já existe a sessão de treino {string} para o jogo {string}")
    //Cria uma sessão de treino existente para o jogo atual
    public void criarSessaoExistente(String nomeSessao, String nomeJogo) {
        if (partidaAtual == null) {
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
        SessaoTreino sessaoExistente = new SessaoTreino(nomeSessao, partidaAtual);
        repo.salvar(sessaoExistente);
    }

    @Dado("que não existem jogos no calendário")
    //Garante que não há partidas no calendário
    public void limparPartidas() {
        partidaAtual = null;
    }

    @Quando("o treinador cria a sessão de treino {string} para o jogo {string}")
    public void criarSessao(String nomeSessao, String nomeJogo) {
        try {
            sessaoCriada = service.criarSessao(nomeSessao, partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("o treinador cria uma sessão de treino para o jogo {string}")
    public void criarSessaoAuto(String nomeJogo) {
        try {
            sessaoCriada = service.criarSessao("SessaoAuto", partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("o treinador tenta criar uma sessão de treino tático")
    public void criarSessaoTatica() {
        try {
            sessaoCriada = service.criarSessao("TreinoTeste", partidaAtual, jogadores);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Então("o sistema deve permitir a criação da sessão")
    public void verificarSessaoCriada() {
        assertNotNull(sessaoCriada);
        List<SessaoTreino> sessoes = repo.listarPorPartida(partidaAtual.getDescricao());
        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(sessaoCriada.getNome())));
    }

    @Então("o sistema deve impedir a criação da sessão")
    public void verificarFalhaCriacao() {
        assertNull(sessaoCriada);
        assertNotNull(excecao);
    }

    @Então("o jogador {string} deve aparecer na lista de convocação")
    public void jogadorConvocado(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .anyMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)));
    }

    @Então("o jogador {string} não deve aparecer na lista de convocação")
    public void jogadorNaoConvocado(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .noneMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)));
    }

    @Então("a sessão de treino {string} deve estar vinculada ao jogo {string}")
    public void verificarSessaoVinculada(String nomeSessao, String nomeJogo) {
        List<SessaoTreino> sessoes = repo.listarPorPartida(nomeJogo);
        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(nomeSessao)
                        && s.getPartida().getDescricao().equalsIgnoreCase(nomeJogo)));
    }

    @Então("o sistema deve exibir a mensagem {string}")
    public void verificarMensagem(String mensagemEsperada) {
        assertNotNull(excecao);
        assertEquals(mensagemEsperada, excecao.getMessage());
    }
}
