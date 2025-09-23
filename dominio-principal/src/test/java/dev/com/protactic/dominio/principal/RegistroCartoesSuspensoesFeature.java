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
    public void o_atleta_esta_suspenso_por_acumulacao_cartoes_amarelos(String status) {
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
    public void o_analista_executa_processo_limpeza_suspensoes() {
        this.cartoesAmarelos = 0;
        this.cartoesVermelhos = 0;
        this.suspenso = false;
        this.disponivel = this.contratoAtivo;
    }

    private void aplicarRegrasSuspensao() {
        // Regra: 3 cartões amarelos OU 1 cartão vermelho = suspenso
        if (this.contratoAtivo) {
            if (this.cartoesAmarelos >= 3 || this.cartoesVermelhos >= 1) {
                this.suspenso = true;
                this.disponivel = false;
            } else {
                this.suspenso = false;
                this.disponivel = true;
            }
        } else {
            this.disponivel = false; // Atleta com contrato inativo nunca está disponível
        }
    }

    @Então("o atleta deve ter {int} cartões amarelos acumulados")
    public void o_atleta_deve_ter_cartoes_amarelos_acumulados(Integer quantidadeEsperada) {
        assertEquals(quantidadeEsperada, this.cartoesAmarelos, 
            "Quantidade de cartões amarelos não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve ter {int} cartões vermelhos recebidos")
    public void o_atleta_deve_ter_cartoes_vermelhos_recebidos(Integer quantidadeEsperada) {
        assertEquals(quantidadeEsperada, this.cartoesVermelhos,
            "Quantidade de cartões vermelhos não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve permanecer {string} para escalação")
    public void o_atleta_deve_permanecer_para_escalacao(String status) {
        boolean esperadoDisponivel = "disponível".equals(status);
        assertEquals(esperadoDisponivel, this.disponivel,
            "Status de disponibilidade não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve ficar {string} para a próxima escalação")
    public void o_atleta_deve_ficar_para_proxima_escalacao(String status) {
        boolean esperadoSuspenso = "suspenso".equals(status);
        assertEquals(esperadoSuspenso, this.suspenso,
            "Status de suspensão não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve voltar a ficar {string} para escalação")
    public void o_atleta_deve_voltar_a_ficar_para_escalacao(String status) {
        boolean esperadoDisponivel = "disponível".equals(status);
        assertEquals(esperadoDisponivel, this.disponivel,
            "Status de disponibilidade após limpeza não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve permanecer {string} por conta do cartão vermelho")
    public void o_atleta_deve_permanecer_por_conta_do_cartao_vermelho(String status) {
        boolean esperadoSuspenso = "suspenso".equals(status);
        assertEquals(esperadoSuspenso, this.suspenso,
            "Status de suspensão por cartão vermelho não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve permanecer {string} por conta do contrato inativo")
    public void o_atleta_deve_permanecer_por_conta_do_contrato_inativo(String status) {
        boolean esperadoIndisponivel = "indisponível".equals(status);
        assertEquals(esperadoIndisponivel, !this.disponivel,
            "Status de indisponibilidade por contrato inativo não corresponde ao esperado para " + this.atleta);
    }

    @Então("o atleta deve permanecer {string} para a próxima escalação")
    public void o_atleta_deve_permanecer_para_a_proxima_escalacao(String status) {
        boolean esperadoSuspenso = "suspenso".equals(status);
        assertEquals(esperadoSuspenso, this.suspenso,
            "Status de suspensão não corresponde ao esperado para " + this.atleta);
    }
}