package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesMock;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;

public class RegistroLesoesFeature {

    private RegistroLesoesRepository repo;
    private RegistroLesoesServico servico;
    private Exception erro;

    // mantém o atleta do contexto atual (ex.: "AT-001")
    private String atletaCorrente;

    @io.cucumber.java.Before
    public void setup() {
        repo = new RegistroLesoesMock();
        servico = new RegistroLesoesServico(repo);
        erro = null;
        atletaCorrente = null;
    }

    // ===================== Contexto =====================

    @Dado("que existe um atleta identificado por {string}")
    public void que_existe_um_atleta_identificado_por(String atletaId) {
        repo.cadastrarAtleta(atletaId);
        atletaCorrente = atletaId;
        // estado default já é: status="Saudável", disponibilidade="disponível", permissão="liberada"
    }

    // ===================== Cenário 1 =====================

    @Dado("que o atleta {string} está saudável e com contrato ativo")
    public void atleta_saudavel_contrato_ativo(String atletaId) {
        atletaCorrente = atletaId;
        repo.definirContratoAtivo(atletaId, true);
        repo.atualizarStatusAtleta(atletaId, "Saudável");
        repo.atualizarDisponibilidade(atletaId, "disponível");
        repo.atualizarPermissaoTreino(atletaId, "liberada");
    }

    @Quando("registrar uma lesão de grau {int} para o atleta {string}")
    public void registrar_lesao_grau_para_atleta(Integer grau, String atletaId) {
        atletaCorrente = atletaId;
        try {
            servico.registrarLesao(atletaId, grau);
        } catch (Exception e) {
            erro = e;
        }
    }

    @Então("a lesão é registrada com grau {int} e status {string}")
    public void lesao_registrada_com_grau_e_status(Integer grau, String status) {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        var grauOpt = repo.grauLesaoAtiva(id);
        assertTrue(grauOpt.isPresent(), "Deveria haver lesão ativa");
        assertEquals(grau.intValue(), grauOpt.get());
        assertEquals(status, repo.lesaoStatus(id));
    }

    @Então("o status do atleta passa a ser {string}")
    public void status_do_atleta_passa_a_ser(String esperado) {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        assertEquals(esperado, repo.statusAtleta(id));
    }

    @Então("a disponibilidade do atleta para jogos e treinos fica {string}")
    public void disponibilidade_fica(String esperado) {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        assertEquals(esperado, repo.disponibilidadeAtleta(id));
    }

    // ===================== Cenário 2 =====================

    @Dado("que o atleta {string} está saudável e sem contrato ativo")
    public void atleta_saudavel_sem_contrato_ativo(String atletaId) {
        atletaCorrente = atletaId;
        repo.definirContratoAtivo(atletaId, false);
        repo.atualizarStatusAtleta(atletaId, "Saudável");
        repo.atualizarDisponibilidade(atletaId, "disponível");
        repo.atualizarPermissaoTreino(atletaId, "liberada");
    }

    @Quando("tentar registrar uma lesão de grau {int} para o atleta {string}")
    public void tentar_registrar_lesao(Integer grau, String atletaId) {
        atletaCorrente = atletaId;
        try {
            servico.registrarLesao(atletaId, grau);
        } catch (Exception e) {
            erro = e;
        }
    }

    @Então("a lesão não é registrada")
    public void lesao_nao_registrada() {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        assertTrue(repo.grauLesaoAtiva(id).isEmpty(), "Não deveria haver lesão ativa");
    }

    @Então("o sistema de lesões exibe o erro {string}")
    public void sistema_exibe_erro(String mensagem) {
        assertNotNull(erro, "Era esperado erro");
        assertEquals(mensagem, erro.getMessage());
    }

    // ===================== Cenário 3 =====================

    @Dado("que o atleta {string} possui uma lesão de grau {int} registrada e ativa")
    public void atleta_possui_lesao_ativa(String atletaId, Integer grau) {
        atletaCorrente = atletaId;
        // garantir contrato ativo para poder registrar a lesão
        repo.definirContratoAtivo(atletaId, true);
        servico.registrarLesao(atletaId, grau);
    }

    @Quando("cadastrar um plano de recuperação com treinos adaptados por {int} dias")
    public void cadastrar_plano(int dias) {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        try {
            servico.cadastrarPlanoRecuperacao(id, dias);
        } catch (Exception e) {
            erro = e;
        }
    }

    @Então("o plano de recuperação é registrado para o atleta {string}")
    public void plano_registrado_para_atleta(String atletaId) {
        assertTrue(repo.planoDias(atletaId).isPresent(), "Plano deveria estar presente");
    }

    @Então("a permissão de treino do atleta fica {string} até o fim do plano")
    public void permissao_treino_fica(String esperado) {
        String id = atletaCorrente != null ? atletaCorrente : "AT-001";
        assertEquals(esperado, repo.permissaoTreino(id));
    }

    // ===================== Cenário 4 =====================

    @Quando("tentar registrar uma nova lesão para o atleta {string}")
    public void tentar_registrar_nova_lesao_para_atleta(String atletaId) {
        atletaCorrente = atletaId;
        try {
            // qualquer grau; deve barrar por já existir uma lesão ativa
            servico.registrarLesao(atletaId, 1);
        } catch (Exception e) {
            erro = e;
        }
    }

    @Então("a nova lesão não é registrada")
    public void nova_lesao_nao_registrada() {
        assertNotNull(erro, "Era esperado erro por lesão ativa");
    }

    @Então("o sistema solicita finalizar a recuperação da lesão ativa de grau {int}")
    public void sistema_solicita_finalizar_lesao_ativa_grau(Integer grau) {
        assertNotNull(erro, "Era esperado erro");
        assertTrue(
            erro.getMessage().contains("lesão ativa de grau " + grau),
            "Mensagem deveria indicar grau ativo: " + erro.getMessage()
        );
    }
}