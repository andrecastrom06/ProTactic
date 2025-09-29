package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.com.protactic.dominio.principal.registroInscricaoAtleta.InscricaoAtleta;
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoRepositoryFake;
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class RegistroInscricaoAtletaFeature {

    private final RegistroInscricaoService service;
    private InscricaoAtleta inscricaoResult;
    private String competicao;

    public RegistroInscricaoAtletaFeature() {
        this.service = new RegistroInscricaoService(new RegistroInscricaoRepositoryFake());
    }

    // ---------- Passos genéricos ----------

    @Dado("que existe a competição {string} identificada por {string}")
    public void que_existe_a_competicao_identificada_por(String nomeCompeticao, String idCompeticao) {
        this.competicao = idCompeticao;
    }

    @Dado("existe o elenco cadastrado com os jogadores:")
    public void existe_o_elenco_cadastrado_com_os_jogadores(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("DEBUG >> Elenco cadastrado: " + dataTable.asMaps());
    }

    // ---------- Passos específicos para João ----------

    @Dado("que João possui contrato ativo e tem {int} anos")
    public void que_joao_possui_contrato_ativo_e_tem_anos(Integer idade) {
        inscricaoResult = service.registrarInscricao("João", idade, true, competicao);
        System.out.println("DEBUG >> João | Idade: " + idade + " | Contrato ativo");
    }

    @Quando("o analista registrar a inscrição de João na competição {string}")
    public void o_analista_registrar_a_inscricao_de_joao_na_competicao(String competicao) {
        inscricaoResult = service.registrarInscricao("João", inscricaoResult.isInscrito() ? 17 : 17, true, competicao);
        System.out.println("DEBUG >> Registrando João na competição: " + competicao);
    }

    @Então("João fica inscrito na competição {string}")
    public void joao_fica_inscrito_na_competicao(String competicaoEsperada) {
        assertEquals("João", inscricaoResult.getAtleta());
        assertEquals(competicaoEsperada, inscricaoResult.getCompeticao());
        assertTrue(inscricaoResult.isInscrito());
        assertNull(inscricaoResult.getMensagemErro());
    }

    // ---------- Passos específicos para Pedro ----------

    @Dado("que Pedro possui contrato ativo e tem {int} anos")
    public void que_pedro_possui_contrato_ativo_e_tem_anos(Integer idade) {
        inscricaoResult = service.registrarInscricao("Pedro", idade, true, competicao);
        System.out.println("DEBUG >> Pedro | Idade: " + idade + " | Contrato ativo");
    }

    @Quando("o analista tentar registrar a inscrição de Pedro na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_pedro_na_competicao(String competicao) {
        inscricaoResult = service.registrarInscricao("Pedro", inscricaoResult.isInscrito() ? 15 : 15, true, competicao);
        System.out.println("DEBUG >> Tentando registrar Pedro na competição: " + competicao);
    }

    // ---------- Passos específicos para Lucas ----------

    @Dado("que Lucas tem {int} anos mas não possui contrato ativo")
    public void que_lucas_tem_anos_mas_nao_possui_contrato_ativo(Integer idade) {
        inscricaoResult = service.registrarInscricao("Lucas", idade, false, competicao);
        System.out.println("DEBUG >> Lucas | Idade: " + idade + " | Sem contrato ativo");
    }

    @Quando("o analista tentar registrar a inscrição de Lucas na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_lucas_na_competicao(String competicao) {
        inscricaoResult = service.registrarInscricao("Lucas", inscricaoResult.isInscrito() ? 18 : 18, false, competicao);
        System.out.println("DEBUG >> Tentando registrar Lucas na competição: " + competicao);
    }

    // ---------- Passos genéricos de verificação ----------

    @Então("passa a estar elegível para jogos dessa competição")
    public void jogador_elegivel_para_jogos() {
        assertTrue(inscricaoResult.isElegivelParaJogos());
    }

    @Então("o sistema não permite o registro")
    public void sistema_nao_permite_registro() {
        assertFalse(inscricaoResult.isInscrito());
        assertNotNull(inscricaoResult.getMensagemErro());
    }

    @Então("o sistema exibe o erro {string}")
    public void sistema_exibe_erro(String mensagemEsperada) {
        assertEquals(mensagemEsperada, inscricaoResult.getMensagemErro());
    }
}
