package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegistroLesoesFeature {

    // ====================== "Banco" simples por atleta ======================
    private final Map<String, Contrato> contratos = new HashMap<>();
    private final Map<String, Lesao> lesoes = new HashMap<>();

    // Estado lógico para as asserções (desacoplado dos POJOs)
    private final Map<String, Boolean> contratoAtivo = new HashMap<>();
    private final Map<String, Boolean> lesaoAtiva = new HashMap<>();
    private final Map<String, Integer> grauLesao = new HashMap<>();
    private final Map<String, String> statusLesao = new HashMap<>();
    private final Map<String, String> statusAtleta = new HashMap<>();
    private final Map<String, String> disponibilidade = new HashMap<>();
    private final Map<String, String> permissaoTreino = new HashMap<>();
    private final Map<String, Boolean> planoRecuperacaoAtivo = new HashMap<>();
    private final Map<String, Integer> planoRecuperacaoDias = new HashMap<>();
    private final Map<String, String> ultimoErro = new HashMap<>();

    private String atletaEmContexto; // último atleta citado (ex.: "AT-001")

    // ====================== Helpers ======================
    private static Contrato novoContratoAtivo(boolean ativo) {
        // Contrato(int id, int duracaoMeses, double salario, String status, Clube clube)
        return new Contrato(
                0,                       // id fictício
                0,                       // duração irrelevante aqui
                0.0,                     // salário irrelevante aqui
                ativo ? "ativo" : "inativo",
                null                     // clube não é relevante nestes testes
        );
    }

    private static Lesao novaLesao(int grau, boolean ativa) {
        // Lesao(int id, Jogador jogador, boolean lesionado, String tempo, String plano, int grau)
        return new Lesao(
                0,        // id fictício
                null,     // não precisamos de Jogador aqui
                ativa,    // lesionado/ativa
                null,     // tempo só é relevante se houver plano
                null,     // plano idem
                grau
        );
    }

    private String atual() {
        return atletaEmContexto != null ? atletaEmContexto : "AT-001";
    }
    private static boolean b(Boolean v) { return v != null && v; }

    private boolean podeRegistrarLesao(String id) {
        if (!b(contratoAtivo.get(id))) {
            ultimoErro.put(id, "Contrato inativo impede o registro de lesão");
            return false;
        }
        if (b(lesaoAtiva.get(id))) {
            Integer g = grauLesao.get(id);
            ultimoErro.put(id, "Finalize a recuperação da lesão ativa de grau " + (g == null ? "?" : g));
            return false;
        }
        return true;
    }

    private boolean registrarLesao(String id, int grau) {
        if (!podeRegistrarLesao(id)) return false;

        // Estado lógico
        lesaoAtiva.put(id, true);
        grauLesao.put(id, grau);
        statusLesao.put(id, "ativa");
        statusAtleta.put(id, "Lesionado (grau " + grau + ")");
        disponibilidade.put(id, "indisponível");
        permissaoTreino.put(id, "liberado"); // vira "limitada" quando cadastrar plano
        planoRecuperacaoAtivo.put(id, false);
        planoRecuperacaoDias.put(id, 0);
        ultimoErro.put(id, null);

        // POJO Lesao: cria/atualiza conforme o construtor existente
        Lesao l = lesoes.get(id);
        if (l == null) {
            l = novaLesao(grau, true);
            lesoes.put(id, l);
        } else {
            l.setGrau(grau);
            l.setLesionado(true);
        }
        return true;
    }

    private boolean cadastrarPlanoRecuperacao(String id, int dias) {
        if (!b(lesaoAtiva.get(id))) {
            ultimoErro.put(id, "Não há lesão ativa para planejar recuperação");
            return false;
        }
        planoRecuperacaoAtivo.put(id, true);
        planoRecuperacaoDias.put(id, dias);
        permissaoTreino.put(id, "limitada");
        ultimoErro.put(id, null);

        // POJO Lesao: se você quiser refletir "plano" e/ou "tempo" na entidade
        Lesao l = lesoes.get(id);
        if (l != null) {
            l.setPlano("Treinos adaptados");
            l.setTempo(dias + " dias");
        }
        return true;
    }

    // ====================== Background ======================
    @Dado("que existe um atleta identificado por {string}")
    public void que_existe_um_atleta_identificado_por(String id) {
        atletaEmContexto = id;
        // não precisamos instanciar Jogador/Contrato/Lesao aqui
    }

    // ====================== DADO ======================
    @Dado("que o atleta {string} está saudável e com contrato ativo")
    public void atleta_saudavel_com_contrato_ativo(String id) {
        atletaEmContexto = id;

        contratoAtivo.put(id, true);
        contratos.put(id, novoContratoAtivo(true));

        lesaoAtiva.put(id, false);
        grauLesao.put(id, -1);
        statusLesao.put(id, "inexistente");
        statusAtleta.put(id, "Saudável");
        disponibilidade.put(id, "disponível");
        permissaoTreino.put(id, "liberado");
        planoRecuperacaoAtivo.put(id, false);
        planoRecuperacaoDias.put(id, 0);
        ultimoErro.put(id, null);

        // Se já havia uma lesão criada anteriormente, marca como não lesionado
        Lesao l = lesoes.get(id);
        if (l != null) {
            l.setLesionado(false);
            l.setPlano(null);
            l.setTempo(null);
        }
    }

    @Dado("que o atleta {string} está saudável e sem contrato ativo")
    public void atleta_saudavel_sem_contrato_ativo(String id) {
        atletaEmContexto = id;

        contratoAtivo.put(id, false);
        contratos.put(id, novoContratoAtivo(false));

        lesaoAtiva.put(id, false);
        grauLesao.put(id, -1);
        statusLesao.put(id, "inexistente");
        statusAtleta.put(id, "Saudável");
        disponibilidade.put(id, "disponível");
        permissaoTreino.put(id, "liberado");
        planoRecuperacaoAtivo.put(id, false);
        planoRecuperacaoDias.put(id, 0);
        ultimoErro.put(id, null);

        Lesao l = lesoes.get(id);
        if (l != null) {
            l.setLesionado(false);
            l.setPlano(null);
            l.setTempo(null);
        }
    }

    @Dado("que o atleta {string} possui uma lesão de grau {int} registrada e ativa")
    public void atleta_possui_uma_lesao_ativa(String id, Integer grau) {
        atletaEmContexto = id;

        contratoAtivo.put(id, true);
        contratos.put(id, novoContratoAtivo(true));

        boolean ok = registrarLesao(id, grau);
        assertTrue(ok, "Pré-condição inválida: " + ultimoErro.get(id));
        assertTrue(b(lesaoAtiva.get(id)), "Lesão deveria estar ativa.");
        assertEquals(grau.intValue(), grauLesao.get(id));
    }

    // ====================== QUANDO ======================
    @Quando("registrar uma lesão de grau {int} para o atleta {string}")
    public void registrar_uma_lesao_de_grau_para_o_atleta(Integer grau, String id) {
        atletaEmContexto = id;
        contratos.putIfAbsent(id, novoContratoAtivo(true)); // caso não tenha sido setado antes
        contratoAtivo.putIfAbsent(id, true);
        registrarLesao(id, grau);
    }

    @Quando("tentar registrar uma lesão de grau {int} para o atleta {string}")
    public void tentar_registrar_uma_lesao_de_grau_para_o_atleta(Integer grau, String id) {
        atletaEmContexto = id;
        // só tenta; erros/efeitos serão verificados nos passos Então
        registrarLesao(id, grau);
    }

    @Quando("cadastrar um plano de recuperação com treinos adaptados por {int} dias")
    public void cadastrar_um_plano_de_recuperacao_com_treinos_adaptados_por_dias(Integer dias) {
        String id = atual(); // no feature, o contexto é sempre "AT-001"
        boolean ok = cadastrarPlanoRecuperacao(id, dias);
        assertTrue(ok, "Falha ao cadastrar plano de recuperação: " + ultimoErro.get(id));
    }

    @Quando("tentar registrar uma nova lesão para o atleta {string}")
    public void tentar_registrar_uma_nova_lesao_para_o_atleta(String id) {
        atletaEmContexto = id;
        registrarLesao(id, 1); // grau arbitrário; regra impede se já houver ativa
    }

    // ====================== ENTÃO ======================
    @Então("a lesão é registrada com grau {int} e status {string}")
    public void a_lesao_e_registrada_com_grau_e_status(Integer grau, String status) {
        String id = atual();
        assertTrue(b(lesaoAtiva.get(id)), "Lesão não está ativa.");
        assertEquals(grau.intValue(), grauLesao.get(id), "Grau da lesão diferente do esperado.");
        assertEquals(status, statusLesao.get(id), "Status da lesão diferente do esperado.");
    }

    @Então("o status do atleta passa a ser {string}")
    public void o_status_do_atleta_passa_a_ser(String statusEsperado) {
        String id = atual();
        assertEquals(statusEsperado, statusAtleta.get(id), "Status do atleta diferente do esperado.");
    }

    @Então("a disponibilidade do atleta para jogos e treinos fica {string}")
    public void a_disponibilidade_do_atleta_para_jogos_e_treinos_fica(String disponibilidadeEsperada) {
        String id = atual();
        assertEquals(disponibilidadeEsperada, disponibilidade.get(id), "Disponibilidade diferente do esperado.");
    }

    @Então("a lesão não é registrada")
    public void a_lesao_nao_e_registrada() {
        String id = atual();
        if (grauLesao.getOrDefault(id, -1) == -1) {
            assertFalse(b(lesaoAtiva.get(id)), "Não deveria haver lesão ativa.");
        } else {
            assertTrue(b(lesaoAtiva.get(id)), "A lesão ativa existente não deveria ter sido removida.");
        }
    }

    @Então("o sistema de lesões exibe o erro {string}")
    public void o_sistema_exibe_o_erro(String mensagemEsperada) {
        String id = atletaEmContexto != null ? atletaEmContexto : "AT-001";
        assertEquals(mensagemEsperada, ultimoErro.get(id), "Mensagem de erro diferente da esperada.");;
    }

    @Então("o plano de recuperação é registrado para o atleta {string}")
    public void o_plano_de_recuperacao_e_registrado_para_o_atleta(String id) {
        assertTrue(b(planoRecuperacaoAtivo.get(id)), "Plano de recuperação não foi registrado.");
        assertTrue(planoRecuperacaoDias.getOrDefault(id, 0) > 0, "Plano de recuperação deve ter duração positiva.");
    }

    @Então("a permissão de treino do atleta fica {string} até o fim do plano")
    public void a_permissao_de_treino_do_atleta_fica_ate_o_fim_do_plano(String permissaoEsperada) {
        String id = atual();
        assertTrue(b(planoRecuperacaoAtivo.get(id)), "Plano de recuperação deveria estar ativo.");
        assertEquals(permissaoEsperada, permissaoTreino.get(id), "Permissão de treino diferente do esperado.");
    }

    @Então("a nova lesão não é registrada")
    public void a_nova_lesao_nao_e_registrada() {
        a_lesao_nao_e_registrada();
    }

    @Então("o sistema solicita finalizar a recuperação da lesão ativa de grau {int}")
    public void o_sistema_solicita_finalizar_a_recuperacao_da_lesao_ativa_de_grau(Integer grau) {
        String id = atual();
        String esperado = "Finalize a recuperação da lesão ativa de grau " + grau;
        assertEquals(esperado, ultimoErro.get(id), "Mensagem de bloqueio para múltiplas lesões diferente do esperado.");
    }
}
