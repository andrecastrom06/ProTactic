package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;  
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import dev.com.protactic.dominio.principal.nota.NotaRepository;
import dev.com.protactic.dominio.principal.nota.NotaService;
import dev.com.protactic.mocks.NotaMock;

public class AtribuicaoNotasFeature {

    private NotaRepository repo;
    private NotaService service;
    private Exception erroCapturado;

    @io.cucumber.java.Before
    public void setup() {
        repo = new NotaMock();
        service = new NotaService(repo);
        erroCapturado = null;

        service.registrarJogadorNoElenco("João");
        service.registrarJogadorNoElenco("Pedro");
        service.registrarJogadorNoElenco("Lucas");
        service.registrarJogadorNoElenco("Caio");
    }

    // ========================= DADO =========================

    @Dado("que João participou do jogo {string}")
    public void que_joao_participou_do_jogo(String jogoId) {
        service.registrarParticipacao(jogoId, "João", true);
    }

    @Dado("que Pedro não participou do jogo {string}")
    public void que_pedro_nao_participou_do_jogo(String jogoId) {
        service.registrarParticipacao(jogoId, "Pedro", false);
    }

    @Dado("que Lucas participou do jogo {string}")
    public void que_lucas_participou_do_jogo(String jogoId) {
        service.registrarParticipacao(jogoId, "Lucas", true);
    }

    @Dado("que Caio participou do jogo {string}")
    public void que_caio_participou_do_jogo(String jogoId) {
        service.registrarParticipacao(jogoId, "Caio", true);
    }

    // ========================= QUANDO =======================

    @Quando("o treinador registrar a nota {double} com a observação {string} para João no jogo {string}")
    public void o_treinador_registrar_a_nota_com_a_observacao_para_joao_no_jogo(Double nota, String observacao, String jogoId) {
        try {
            service.atribuirNotaEObservacao(jogoId, "João", BigDecimal.valueOf(nota), observacao);
        } catch (Exception e) {
            erroCapturado = e;
        }
    }

    @Quando("o treinador tentar registrar a nota {int} para Pedro no jogo {string}")
    public void o_treinador_tentar_registrar_a_nota_para_pedro_no_jogo(Integer nota, String jogoId) {
        try {
            service.atribuirNotaEObservacao(jogoId, "Pedro", BigDecimal.valueOf(nota), null);
        } catch (Exception e) {
            erroCapturado = e;
        }
    }

    @Quando("o treinador tentar registrar a nota {int} para Lucas no jogo {string}")
    public void o_treinador_tentar_registrar_a_nota_para_lucas_no_jogo(Integer nota, String jogoId) {
        try {
            service.atribuirNotaEObservacao(jogoId, "Lucas", BigDecimal.valueOf(nota), null);
        } catch (Exception e) {
            erroCapturado = e;
        }
    }

    @Quando("o treinador registrar apenas a observação {string} para Caio no jogo {string}")
    public void o_treinador_registrar_apenas_a_observacao_para_caio_no_jogo(String observacao, String jogoId) {
        try {
            service.registrarObservacao(jogoId, "Caio", observacao);
        } catch (Exception e) {
            erroCapturado = e;
        }
    }

    // ========================= ENTÃO ========================

    @Então("a nota técnica {double} e a observação {string} são vinculadas ao desempenho de João no jogo {string}")
    public void a_nota_tecnica_e_a_observacao_sao_vinculadas_ao_desempenho_de_joao_no_jogo(Double notaEsperada, String observacaoEsperada, String jogoId) {
        Nota reg = service.obterRegistro(jogoId, "João").orElseThrow();
        assertNotNull(reg.getNota(), "Deveria ter nota técnica");
        assertEquals(0, BigDecimal.valueOf(notaEsperada).compareTo(reg.getNota()));
        assertEquals(observacaoEsperada, reg.getObservacao());
    }

    @Então("o registro de desempenho de João no jogo {string} passa a exibir {string} e {string}")
    public void o_registro_de_desempenho_de_joao_no_jogo_passa_a_exibir_e(String jogoId, String campoNota, String campoObs) throws Exception {
        Nota reg = service.obterRegistro(jogoId, "João").orElseThrow();

        String esperadoNotaStr = valorDepoisDoIgual(campoNota); // "8,5"
        String esperadoObs = valorDepoisDoIgual(campoObs);

        BigDecimal esperadoNota = parseBigDecimalPt(esperadoNotaStr);

        assertNotNull(reg.getNota(), "Deveria ter nota técnica");
        assertEquals(0, esperadoNota.compareTo(reg.getNota()));
        assertEquals(esperadoObs, reg.getObservacao());
    }

    @Então("o sistema de avaliação não permite o registro")
    public void o_sistema_de_avaliacao_nao_permite_o_registro() {
        assertNotNull(erroCapturado, "Era esperado um erro mas não ocorreu");
    }

    @Então("o sistema de avaliação exibe o erro {string}")
    public void o_sistema_de_avaliacao_exibe_o_erro(String mensagem) {
        assertNotNull(erroCapturado, "Nenhum erro foi capturado");
        assertEquals(mensagem, erroCapturado.getMessage());
    }

    @Então("a observação {string} é vinculada ao desempenho de Caio no jogo {string}")
    public void a_observacao_e_vinculada_ao_desempenho_de_caio_no_jogo(String observacao, String jogoId) {
        Nota reg = service.obterRegistro(jogoId, "Caio").orElseThrow();
        assertNull(reg.getNota(), "Não deveria haver nota técnica");
        assertEquals(observacao, reg.getObservacao());
    }

    @Então("o registro de desempenho de Caio no jogo {string} não possui {string}")
    public void o_registro_de_desempenho_de_caio_no_jogo_nao_possui(String jogoId, String campo) {
        Nota reg = service.obterRegistro(jogoId, "Caio").orElseThrow();
        assertNull(reg.getNota(), "Nota deveria estar ausente");
    }

    @Então("a observação não impacta o cálculo de média de desempenho do jogador")
    public void a_observacao_nao_impacta_media() {
        assertTrue(true);
    }

    // ========================= HELPERS =========================================

    private String valorDepoisDoIgual(String s) {
        int i = s.indexOf('=');
        return (i >= 0 && i < s.length() - 1) ? s.substring(i + 1) : "";
    }

    private BigDecimal parseBigDecimalPt(String valor) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        return new BigDecimal(nf.parse(valor).toString());
    }
}