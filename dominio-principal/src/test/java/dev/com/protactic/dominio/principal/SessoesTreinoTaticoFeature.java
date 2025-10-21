package dev.com.protactic.dominio.principal;

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
    public void iniciarPlanejamento() {
        sessaoCriada = null;
        excecao = null;
        jogadores.clear();
        partidaAtual = null;
        jogadorAtual = null;
        repo.limpar(); //garante repositório limpo entre cenários
    }

    @Dado("que existe o jogo {string} no calendário")
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
    public void criarJogador(String nomeJogador, String status) {
        jogadorAtual = new Jogador(nomeJogador);
        jogadorAtual.setStatus(status);
        jogadores.add(jogadorAtual);
    }

    @Dado("que já existe a sessão de treino {string} para o jogo {string}")
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
        assertNotNull(sessaoCriada, "A sessão deveria ter sido criada");
        List<SessaoTreino> sessoes = repo.listarPorPartida(partidaAtual.getDescricao());
        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(sessaoCriada.getNome())),
                "A sessão criada não foi encontrada no repositório");

        //verificação de persistência
        SessaoTreino persistida = repo.getUltimaSessaoSalva();
        assertNotNull(persistida, "A sessão não foi persistida no mock");
        assertEquals(sessaoCriada.getNome(), persistida.getNome(), "Sessão persistida incorreta");
        assertEquals(partidaAtual.getDescricao(), persistida.getPartida().getDescricao(),
                "A sessão persistida não está vinculada ao jogo correto");
    }

    @Então("o sistema deve impedir a criação da sessão")
    public void verificarFalhaCriacao() {
        assertNull(sessaoCriada, "Nenhuma sessão deveria ser criada");
        assertNotNull(excecao, "Uma exceção deveria ter sido lançada");

        //persistência: não deve haver nenhuma sessão salva
        assertNull(repo.getUltimaSessaoSalva(),
                "Nenhuma sessão deveria ter sido persistida no mock");
    }

    @Então("o jogador {string} deve aparecer na lista de convocação")
    public void jogadorConvocado(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .anyMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)),
                "O jogador " + nomeJogador + " deveria estar convocado");
    }

    @Então("o jogador {string} não deve aparecer na lista de convocação")
    public void jogadorNaoConvocado(String nomeJogador) {
        assertTrue(sessaoCriada.getConvocados().stream()
                .noneMatch(j -> j.getNome().equalsIgnoreCase(nomeJogador)),
                "O jogador " + nomeJogador + " não deveria estar convocado");
    }

    @Então("a sessão de treino {string} deve estar vinculada ao jogo {string}")
    public void verificarSessaoVinculada(String nomeSessao, String nomeJogo) {
        List<SessaoTreino> sessoes = repo.listarPorPartida(nomeJogo);
        assertTrue(sessoes.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(nomeSessao)
                        && s.getPartida().getDescricao().equalsIgnoreCase(nomeJogo)),
                "Sessão não encontrada ou vinculada incorretamente ao jogo");
        //persistência: confirma que a última salva pertence ao jogo certo
        SessaoTreino persistida = repo.getUltimaSessaoSalva();
        assertNotNull(persistida, "Nenhuma sessão persistida");
        assertEquals(nomeJogo, persistida.getPartida().getDescricao(),
                "A última sessão persistida não está associada ao jogo correto");
    }

    @Então("o sistema deve exibir a mensagem {string}")
    public void verificarMensagem(String mensagemEsperada) {
        assertNotNull(excecao, "Era esperada uma exceção");
        assertEquals(mensagemEsperada, excecao.getMessage(), "Mensagem de erro incorreta");
        //persistência: não deve haver sessão salva em erro
        assertNull(repo.getUltimaSessaoSalva(),
                "Nenhuma sessão deveria ter sido persistida em caso de erro");
    }
}
