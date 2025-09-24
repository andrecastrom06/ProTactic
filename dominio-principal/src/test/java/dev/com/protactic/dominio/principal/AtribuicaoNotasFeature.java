package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Steps para:
 * 9 - Atribuição de nota técnica e observações
 *
 * Regras:
 * - Só quem atuou pode receber nota técnica.
 * - Nota deve estar entre 0 e 10 (inclusive).
 * - Observações podem ser registradas com ou sem nota.
 * - Observações sem nota NÃO entram na média.
 */
public class AtribuicaoNotasFeature {

    // ======= Estruturas simples de apoio =======
    private static class Desempenho {
        Boolean participou = null;   // por jogo
        Double notaTecnica = null;   // 0..10
        String observacao = null;
    }

    // jogoId -> nomeAmigavel
    private final Map<String, String> jogos = new HashMap<>();
    // nomeJogador -> posição
    private final Map<String, String> elenco = new HashMap<>();
    // nomeJogador -> (jogoId -> desempenho)
    private final Map<String, Map<String, Desempenho>> desempenhos = new HashMap<>();

    // Controle de última operação para steps "não permite" / erro
    private boolean ultimaOperacaoPermitida = true;
    private String ultimoErro = null;

    private String jogoEmContexto; // guarda o último jogo referenciado (opcional)

    // ======= Helpers =======
    private static Double parseNotaPT(String s) {
        if (s == null) return null;
        s = s.trim().replace(',', '.');
        return Double.parseDouble(s);
    }

    private Desempenho getDesempenho(String jogador, String jogoId) {
        Map<String, Desempenho> porJogo = desempenhos.computeIfAbsent(jogador, k -> new HashMap<>());
        return porJogo.computeIfAbsent(jogoId, k -> new Desempenho());
    }

    private double mediaDeNotas(String jogador) {
        Map<String, Desempenho> porJogo = desempenhos.get(jogador);
        if (porJogo == null) return Double.NaN;
        int n = 0;
        double soma = 0.0;
        for (Desempenho d : porJogo.values()) {
            if (d.notaTecnica != null) {
                soma += d.notaTecnica;
                n++;
            }
        }
        return n == 0 ? Double.NaN : (soma / n);
    }

    private static boolean jogadorAtuou(Desempenho d) {
        return d.participou != null && d.participou;
    }

    private static boolean notaValida(double nota) {
        return nota >= 0.0 && nota <= 10.0;
    }

    // ======= Contexto =======
    @Dado("que existe o jogo {string} identificado por {string}")
    public void que_existe_o_jogo_identificado_por(String nome, String jogoId) {
        jogos.put(jogoId, nome);
        jogoEmContexto = jogoId;
    }

    @E("existe o elenco de avaliação cadastrado com os jogadores:")
    public void existe_o_elenco_cadastrado_com_os_jogadores(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> linhas = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : linhas) {
            String nome = row.getOrDefault("nome", "").trim();
            String pos = row.getOrDefault("posição", row.getOrDefault("posicao", "")).trim();
            if (!nome.isEmpty()) {
                elenco.put(nome, pos);
            }
        }
    }

    // ======= Dado =======  (evita ambiguidade com 'não participou')
    @Dado("^que\\s+([^\\s]+)\\s+participou do jogo \"([^\"]+)\"$")
    public void jogador_participou_do_jogo(String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        d.participou = true;
    }

    @Dado("^que\\s+([^\\s]+)\\s+não participou do jogo \"([^\"]+)\"$")
    public void jogador_nao_participou_do_jogo(String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        d.participou = false;
    }

    // ======= Quando =======
    // Nota com vírgula + observação + jogador SEM aspas + jogo COM aspas
    @Quando("^o treinador registrar a nota ([0-9]+(?:,[0-9]+)?) com a observação \"([^\"]+)\" para ([^\\s]+) no jogo \"([^\"]+)\"$")
    public void registrar_nota_com_observacao_para_no_jogo(String notaTxt, String obs, String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        Double nota = parseNotaPT(notaTxt);

        if (!jogadorAtuou(d)) {
            ultimaOperacaoPermitida = false;
            ultimoErro = "Apenas jogadores que atuaram podem receber nota técnica";
            return;
        }
        if (nota == null || !notaValida(nota)) {
            ultimaOperacaoPermitida = false;
            ultimoErro = "Nota inválida. Informe um valor entre 0 e 10";
            return;
        }

        d.notaTecnica = nota;
        d.observacao = obs;
        ultimaOperacaoPermitida = true;
        ultimoErro = null;
    }

    // Tentar registrar nota inteira (7, 12 etc.) — jogador SEM aspas
    @Quando("^o treinador tentar registrar a nota (\\d+) para ([^\\s]+) no jogo \"([^\"]+)\"$")
    public void tentar_registrar_nota_inteira_para_no_jogo(Integer nota, String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);

        if (!jogadorAtuou(d)) {
            ultimaOperacaoPermitida = false;
            ultimoErro = "Apenas jogadores que atuaram podem receber nota técnica";
            return;
        }
        if (nota == null || !notaValida(nota.doubleValue())) {
            ultimaOperacaoPermitida = false;
            ultimoErro = "Nota inválida. Informe um valor entre 0 e 10";
            return;
        }

        d.notaTecnica = nota.doubleValue();
        ultimaOperacaoPermitida = true;
        ultimoErro = null;
    }

    // Registrar apenas observação (sem nota) — permitido sempre
    @Quando("^o treinador registrar apenas a observação \"([^\"]+)\" para ([^\\s]+) no jogo \"([^\"]+)\"$")
    public void registrar_apenas_observacao_para_no_jogo(String obs, String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        d.observacao = obs; // não entra na média
        ultimaOperacaoPermitida = true;
        ultimoErro = null;
    }

    // ======= Então =======
    // Verifica vínculo nota (com vírgula) + observação — jogador SEM aspas
    @Então("^a nota técnica ([0-9]+(?:,[0-9]+)?) e a observação \"([^\"]+)\" são vinculadas ao desempenho de ([^\\s]+) no jogo \"([^\"]+)\"$")
    public void a_nota_e_observacao_sao_vinculadas(String notaTxt, String obs, String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        Double esperado = parseNotaPT(notaTxt);
        assertNotNull(d.notaTecnica, "Nota técnica deveria ter sido registrada.");
        assertEquals(esperado, d.notaTecnica, 1e-9);
        assertEquals(obs, d.observacao, "Observação difere do esperado.");
    }

    @Então("^o registro de desempenho de ([^\\s]+) no jogo \"([^\"]+)\" passa a exibir \"([^\"]+)\" e \"([^\"]+)\"$")
    public void registro_de_desempenho_passara_a_exibir(String jogador, String jogoId, String kv1, String kv2) {
        Desempenho d = getDesempenho(jogador, jogoId);
        String realNota = "nota_tecnica=" + (d.notaTecnica == null ? "" : String.valueOf(d.notaTecnica).replace('.', ','));
        String realObs  = "observacao=" + (d.observacao == null ? "" : d.observacao);
        assertEquals(kv1, realNota, "Exibição de nota_técnica diferente do esperado.");
        assertEquals(kv2, realObs, "Exibição de observacao diferente do esperado.");
    }

    @Então("o sistema de avaliação não permite o registro")
    public void o_sistema_de_avaliacao_nao_permite_o_registro() {
        assertFalse(ultimaOperacaoPermitida, "O sistema deveria bloquear o registro, mas permitiu.");
    }

    @Então("o sistema de avaliação exibe o erro {string}")
    public void o_sistema_de_avaliacao_exibe_o_erro(String mensagem) {
        assertEquals(mensagem, ultimoErro, "Mensagem de erro diferente da esperada.");
    }

    @Então("^a observação \"([^\"]+)\" é vinculada ao desempenho de ([^\\s]+) no jogo \"([^\"]+)\"$")
    public void a_observacao_e_vinculada(String obs, String jogador, String jogoId) {
        Desempenho d = getDesempenho(jogador, jogoId);
        assertEquals(obs, d.observacao, "Observação não foi vinculada corretamente.");
    }

    @Então("^o registro de desempenho de ([^\\s]+) no jogo \"([^\"]+)\" não possui \"([^\"]+)\"$")
    public void registro_nao_possui_chave(String jogador, String jogoId, String chave) {
        Desempenho d = getDesempenho(jogador, jogoId);
        if ("nota_tecnica".equalsIgnoreCase(chave)) {
            assertNull(d.notaTecnica, "Não deveria haver nota técnica registrada.");
        } else if ("observacao".equalsIgnoreCase(chave)) {
            assertNull(d.observacao, "Não deveria haver observação registrada.");
        } else {
            fail("Chave não reconhecida no teste: " + chave);
        }
    }

    @Então("a observação não impacta o cálculo de média de desempenho do jogador")
    public void a_observacao_nao_impacta_media() {
        double media = mediaDeNotas("Caio");
        assertTrue(Double.isNaN(media), "A média não deveria considerar observações sem nota.");
    }
}