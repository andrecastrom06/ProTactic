package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class RegistroCartoesSuspensoesFeature {

private String atleta;
private boolean contratoAtivo;
private int cartoesAmarelos;
private int cartoesVermelhos;
private boolean suspenso;
private boolean disponivel;

@Dado("um atleta chamado {string}")
public void um_atleta_chamado(String nomeAtleta) {
    this.atleta = nomeAtleta;
    this.suspenso = false;
    this.disponivel = true;
}

@Dado("o atleta possui contrato {string} com o clube")
public void o_atleta_possui_contrato_com_o_clube(String statusContrato) {
    this.contratoAtivo = "ativo".equals(statusContrato);
    this.disponivel = this.contratoAtivo && !this.suspenso;
}

@Dado("o atleta tem {int} cartões amarelos acumulados")
public void o_atleta_tem_cartoes_amarelos_acumulados(Integer quantidade) {
    this.cartoesAmarelos = quantidade;
}

@Dado("o atleta tem {int} cartões vermelhos recebidos")
public void o_atleta_tem_cartoes_vermelhos_recebidos(Integer quantidade) {
    this.cartoesVermelhos = quantidade;
}

@Dado("o atleta está {string} por acumulação de cartões amarelos")
public void o_atleta_esta_por_acumulacao_de_cartoes_amarelos(String status) {
    this.suspenso = "suspenso".equals(status);
    this.disponivel = this.contratoAtivo && !this.suspenso;
}

@Dado("a suspensão anterior do atleta foi limpa")
public void a_suspensao_anterior_do_atleta_foi_limpa() {
    this.cartoesAmarelos = 0;
    this.cartoesVermelhos = 0;
    this.suspenso = false;
    this.disponivel = this.contratoAtivo;
}

@Quando("o analista registra um cartão {string} para o atleta")
public void o_analista_registra_um_cartao_para_o_atleta(String tipoCartao) {
    if ("amarelo".equals(tipoCartao)) {
        this.cartoesAmarelos++;
    } else if ("vermelho".equals(tipoCartao)) {
        this.cartoesVermelhos++;
    }
    aplicarRegrasSuspensao();
}

@Quando("o analista executa o processo de limpeza de suspensões")
public void o_analista_executa_o_processo_de_limpeza() {
    this.cartoesAmarelos = 0;
    this.cartoesVermelhos = 0;
    this.suspenso = false;
    this.disponivel = this.contratoAtivo;
}

private void aplicarRegrasSuspensao() {
    if (this.contratoAtivo) {
        if (this.cartoesAmarelos >= 3 || this.cartoesVermelhos >= 1) {
            this.suspenso = true;
            this.disponivel = false;
        } else {
            this.suspenso = false;
            this.disponivel = true;
        }
    } else {
        this.disponivel = false;
    }
}

// ===== Validações =====

@Então("o atleta deve ter {int} cartões amarelos acumulados")
public void validar_cartoes_amarelos(Integer esperado) {
    assertEquals(esperado, this.cartoesAmarelos,
        "Quantidade de cartões amarelos não corresponde ao esperado para " + this.atleta);
}

@Então("o atleta deve ter {int} cartões vermelhos recebidos")
public void validar_cartoes_vermelhos(Integer esperado) {
    assertEquals(esperado, this.cartoesVermelhos,
        "Quantidade de cartões vermelhos não corresponde ao esperado para " + this.atleta);
}

@Então("o atleta deve permanecer {string} para escalação")
@Então("o atleta deve permanecer {string} para a próxima escalação")
@Então("o atleta deve voltar a ficar {string} para escalação")
public void validar_disponibilidade(String status) {
    boolean esperadoDisponivel = "disponível".equals(status);
    assertEquals(esperadoDisponivel, this.disponivel,
        "Status de disponibilidade não corresponde ao esperado para " + this.atleta);
}

@Então("o atleta deve ficar {string} para a próxima escalação")
@Então("o atleta deve permanecer {string} por conta do cartão vermelho")
public void validar_suspensao(String status) {
    boolean esperadoSuspenso = "suspenso".equals(status);
    assertEquals(esperadoSuspenso, this.suspenso,
        "Status de suspensão não corresponde ao esperado para " + this.atleta);
}

@Então("o atleta deve permanecer {string} por conta do contrato inativo")
public void validar_indisponibilidade_contrato(String status) {
    boolean esperadoIndisponivel = "indisponível".equals(status);
    assertEquals(esperadoIndisponivel, !this.disponivel,
        "Status de indisponibilidade por contrato inativo não corresponde ao esperado para " + this.atleta);
}

}
