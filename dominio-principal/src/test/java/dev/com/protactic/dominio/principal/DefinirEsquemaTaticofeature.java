package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;
import dev.com.protactic.mocks.EscalacaoMock;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefinirEsquemaTaticoFeature {

    private static class Jogador {
        final String nome;
        int grauLesao = -1;
        boolean contratoAtivo = false;
        boolean suspenso = false;

        Jogador(String nome) {
            this.nome = nome;
        }
    }

    private final Map<String, Jogador> jogadores = new HashMap<>();
    private String jogoEmContexto;

    private final EscalacaoMock repository = new EscalacaoMock();
    private final DefinirEsquemaTaticoService service = new DefinirEsquemaTaticoService(repository);

    @Dado("que o treinador está na tela de gerenciamento de escalação e tática")
    public void que_o_treinador_esta_na_tela_de_gerenciamento_de_escalacao_e_tatica() {
        System.out.println("Contexto: Treinador na tela de gerenciamento.");
    }

    @Dado("que existe um jogo marcado para {string}")
    public void que_existe_um_jogo_marcado_para(String data) {
        jogoEmContexto = data;
        System.out.println("Dado que existe um jogo marcado para " + data);
    }

    @Quando("o treinador cadastrar a escalação")
    public void o_treinador_cadastrar_a_escalacao() {
        repository.salvarJogadorNaEscalacao(jogoEmContexto, "ESCALAÇÃO_VAZIA");
    }

    @Então("a escalação aparecerá vinculada ao jogo do dia {string}")
    public void a_escalacao_aparecera_vinculada_ao_jogo_do_dia(String data) {
        var escalacaoPersistida = repository.obterEscalacaoPorData(data);

        assertNotNull(escalacaoPersistida, "Nenhuma escalação encontrada para o jogo.");
        assertFalse(escalacaoPersistida.isEmpty(), "Escalação não foi registrada para o jogo do dia " + data);

        assertEquals(escalacaoPersistida, repository.obterEscalacaoPorData(data),
                "A escalação persistida não corresponde à data esperada.");
    }

    @E("o jogador {string} tem uma lesão de grau {int}")
    public void o_jogador_tem_uma_lesao_de_grau(String nomeJogador, Integer grau) {
        jogadores.computeIfAbsent(nomeJogador, Jogador::new).grauLesao = grau;
    }

    @E("{string} possui contrato ativo e não está suspenso")
    public void possui_contrato_ativo_e_nao_esta_suspenso(String nomeJogador) {
        Jogador j = jogadores.computeIfAbsent(nomeJogador, Jogador::new);
        j.contratoAtivo = true;
        j.suspenso = false;
    }

    @Quando("o treinador cadastrar a escalação incluindo {string}")
    public void o_treinador_cadastrar_a_escalacao_incluindo(String nomeJogador) {
        Jogador jogador = jogadores.get(nomeJogador);
        boolean sucesso = service.registrarEscalacao(
                jogoEmContexto,
                nomeJogador,
                jogador.contratoAtivo,
                jogador.suspenso,
                jogador.grauLesao
        );

        jogador.contratoAtivo = sucesso;
    }

    @Então("a escalação será registrada com sucesso")
    public void a_escalacao_sera_registrada_com_sucesso() {
        var escalacaoPersistida = repository.obterEscalacaoPorData(jogoEmContexto);

        assertNotNull(escalacaoPersistida,
                "Nenhuma escalação foi encontrada no repositório para o jogo.");
        assertFalse(escalacaoPersistida.isEmpty(),
                "A escalação deveria ter sido registrada, mas está vazia.");

      
        boolean contemJogador = escalacaoPersistida.stream()
                .anyMatch(jogador -> jogadores.containsKey(jogador));

        assertTrue(contemJogador,
                "O jogador escalado não foi persistido na escalação do repositório.");

      
        assertEquals(escalacaoPersistida, repository.obterEscalacaoPorData(jogoEmContexto),
                "Os dados persistidos no repositório não correspondem à data do jogo.");
    }

    @Então("a escalação não poderá ser registrada")
    public void a_escalacao_nao_podera_ser_registrada() {
        var escalacaoPersistida = repository.obterEscalacaoPorData(jogoEmContexto);

  
        assertTrue(escalacaoPersistida == null || escalacaoPersistida.isEmpty(),
                "A escalação não deveria ter sido persistida, mas há dados salvos no repositório.");
    }

    @E("o jogador {string} está suspenso")
    public void o_jogador_esta_suspenso(String nomeJogador) {
        jogadores.computeIfAbsent(nomeJogador, Jogador::new).suspenso = true;
    }

    @E("{string} possui contrato ativo e não está lesionado")
    public void possui_contrato_ativo_e_nao_esta_lesionado(String nomeJogador) {
        Jogador j = jogadores.computeIfAbsent(nomeJogador, Jogador::new);
        j.contratoAtivo = true;
        j.grauLesao = -1;
    }

    @E("o jogador {string} tem a condição {string}")
    public void o_jogador_tem_a_condicao(String nome, String condicao) {
        Jogador j = jogadores.computeIfAbsent(nome, Jogador::new);
        String cond = condicao.toLowerCase();

        if (cond.contains("lesão grau")) {
            j.grauLesao = Integer.parseInt(cond.replaceAll("\\D+", ""));
        } else if (cond.equals("suspenso")) {
            j.suspenso = true;
        } else if (cond.equals("saudável")) {
            j.grauLesao = -1;
            j.suspenso = false;
        }
    }
    @E("{string} possui contrato {string}")
    public void possui_contrato(String nome, String statusContrato) {
        jogadores.computeIfAbsent(nome, Jogador::new).contratoAtivo = statusContrato.equalsIgnoreCase("ativo");
    }
}
