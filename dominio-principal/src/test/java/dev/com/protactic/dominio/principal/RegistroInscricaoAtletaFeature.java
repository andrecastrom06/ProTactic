package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class RegistroInscricaoAtletaFeature {

    private String jogador;
    private int idade;
    private boolean contratoAtivo;
    private String competicao;
    private boolean inscricaoSucesso;
    private String mensagemErro;
    private boolean elegivelParaJogos;

    @Dado("que existe a competição {string} identificada por {string}")
    public void que_existe_a_competicao_identificada_por(String nomeCompeticao, String idCompeticao) {
        this.competicao = idCompeticao;
    }

    @Dado("existe o elenco cadastrado com os jogadores:")
    public void existe_o_elenco_cadastrado_com_os_jogadores(io.cucumber.datatable.DataTable dataTable) {
        // Esta tabela será usada como referência para os cenários específicos
        // Os dados reais serão definidos nos passos específicos de cada cenário
        System.out.println("DEBUG >> Elenco cadastrado: " + dataTable.asMaps());
    }

    @Dado("que João possui contrato ativo e tem {int} anos")
    public void que_joao_possui_contrato_ativo_e_tem_anos(Integer idade) {
        this.jogador = "João";
        this.idade = idade;
        this.contratoAtivo = true;
        this.mensagemErro = null;
        this.inscricaoSucesso = false;
        this.elegivelParaJogos = false;
        System.out.println("DEBUG >> Jogador: " + this.jogador + " | Idade: " + this.idade + " | Contrato: " + this.contratoAtivo);
    }

    @Dado("que Pedro possui contrato ativo e tem {int} anos")
    public void que_pedro_possui_contrato_ativo_e_tem_anos(Integer idade) {
        this.jogador = "Pedro";
        this.idade = idade;
        this.contratoAtivo = true;
        this.mensagemErro = null;
        this.inscricaoSucesso = false;
        this.elegivelParaJogos = false;
        System.out.println("DEBUG >> Jogador: " + this.jogador + " | Idade: " + this.idade + " | Contrato: " + this.contratoAtivo);
    }

    @Dado("que Lucas tem {int} anos mas não possui contrato ativo")
    public void que_lucas_tem_anos_mas_nao_possui_contrato_ativo(Integer idade) {
        this.jogador = "Lucas";
        this.idade = idade;
        this.contratoAtivo = false;
        this.mensagemErro = null;
        this.inscricaoSucesso = false;
        this.elegivelParaJogos = false;
        System.out.println("DEBUG >> Jogador: " + this.jogador + " | Idade: " + this.idade + " | Contrato: " + this.contratoAtivo);
    }

    @Quando("o analista registrar a inscrição de João na competição {string}")
    public void o_analista_registrar_a_inscricao_de_joao_na_competicao(String competicao) {
        this.jogador = "João";
        this.competicao = competicao;
        processarInscricao();
    }

    @Quando("o analista tentar registrar a inscrição de Pedro na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_pedro_na_competicao(String competicao) {
        this.jogador = "Pedro";
        this.competicao = competicao;
        processarInscricao();
    }

    @Quando("o analista tentar registrar a inscrição de Lucas na competição {string}")
    public void o_analista_tentar_registrar_a_inscricao_de_lucas_na_competicao(String competicao) {
        this.jogador = "Lucas";
        this.competicao = competicao;
        processarInscricao();
    }

    private void processarInscricao() {
        // Validação das regras de negócio
        if (idade < 16) {
            this.inscricaoSucesso = false;
            this.mensagemErro = "Jogador menor de 16 anos não pode ser inscrito";
        } else if (!contratoAtivo) {
            this.inscricaoSucesso = false;
            this.mensagemErro = "Jogador sem contrato ativo não pode ser inscrito";
        } else {
            this.inscricaoSucesso = true;
            this.elegivelParaJogos = true;
            this.mensagemErro = null;
        }
        System.out.println("DEBUG >> Inscrição: " + this.inscricaoSucesso + " | Erro: " + this.mensagemErro);
    }

    @Então("João fica inscrito na competição {string}")
    public void joao_fica_inscrito_na_competicao(String competicaoEsperada) {
        assertEquals("João", this.jogador);
        assertEquals(competicaoEsperada, this.competicao);
        assertTrue(this.inscricaoSucesso, "A inscrição deveria ter sido bem sucedida");
        assertNull(this.mensagemErro, "Não deveria haver mensagem de erro");
        System.out.println("DEBUG >> João inscrito com sucesso na competição: " + competicaoEsperada);
    }

    @Então("passa a estar elegível para jogos dessa competição")
    public void passa_a_estar_elegivel_para_jogos_dessa_competicao() {
        assertTrue(this.elegivelParaJogos, "O jogador deveria estar elegível para jogos");
        System.out.println("DEBUG >> Jogador elegível para jogos");
    }

    @Então("o sistema não permite o registro")
    public void o_sistema_nao_permite_o_registro() {
        assertFalse(this.inscricaoSucesso, "A inscrição não deveria ter sido permitida");
        assertNotNull(this.mensagemErro, "Deveria haver uma mensagem de erro");
        System.out.println("DEBUG >> Registro bloqueado: " + this.mensagemErro);
    }

    @Então("o sistema exibe o erro {string}")
    public void o_sistema_exibe_o_erro(String mensagemErroEsperada) {
        assertEquals(mensagemErroEsperada, this.mensagemErro);
        System.out.println("DEBUG >> Erro exibido: " + mensagemErroEsperada);
    }
}