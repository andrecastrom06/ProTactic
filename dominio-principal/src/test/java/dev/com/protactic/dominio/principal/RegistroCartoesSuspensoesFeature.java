package dev.com.protactic.dominio.principal;


import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
import dev.com.protactic.mocks.RegistroCartoesMock;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class RegistroCartoesSuspensoesFeature {

    private String atleta;
    private boolean contratoAtivo;
    private boolean suspenso;
    private boolean disponivel;
    private RegistroCartoesRepository repository;
    private RegistroCartoesService service;

    @Dado("um atleta chamado {string}")
    public void um_atleta_chamado(String nomeAtleta) {
        this.atleta = nomeAtleta;
        this.repository = new RegistroCartoesMock();
        this.service = new RegistroCartoesService(repository);
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
        for (int i = 0; i < quantidade; i++) {
            repository.salvarCartao(new RegistroCartao(atleta, "amarelo"));
        }
    }

    @Dado("o atleta tem {int} cartões vermelhos recebidos")
    public void o_atleta_tem_cartoes_vermelhos_recebidos(Integer quantidade) {
        for (int i = 0; i < quantidade; i++) {
            repository.salvarCartao(new RegistroCartao(atleta, "vermelho"));
        }
    }

    @Dado("o atleta está {string} por acumulação de cartões amarelos")
    public void o_atleta_esta_por_acumulacao_de_cartoes_amarelos(String status) {
        this.suspenso = "suspenso".equals(status);
        this.disponivel = this.contratoAtivo && !this.suspenso;
    }

    @Dado("a suspensão anterior do atleta foi limpa")
    public void a_suspensao_anterior_do_atleta_foi_limpa() {
        service.limparCartoes(atleta);
        this.suspenso = false;
        this.disponivel = this.contratoAtivo;
    }

    @Quando("o analista registra um cartão {string} para o atleta")
    public void o_analista_registra_um_cartao_para_o_atleta(String tipoCartao) {
        service.registrarCartao(atleta, tipoCartao);
        var status = service.verificarSuspensao(atleta); // ✅ regra delegada ao service
        this.suspenso = status.isSuspenso();
        this.disponivel = contratoAtivo && !suspenso;
    }

    @Quando("o analista executa o processo de limpeza de suspensões")
    public void o_analista_executa_o_processo_de_limpeza_de_suspensoes() {
        service.limparCartoes(atleta);
        this.suspenso = false;
        this.disponivel = this.contratoAtivo;
    }

    @Então("o atleta deve ter {int} cartões amarelos acumulados")
    public void validar_cartoes_amarelos(Integer esperado) {
        long amarelos = repository.buscarCartoesPorAtleta(atleta)
                .stream().filter(c -> "amarelo".equals(c.getTipo())).count();
        assertEquals(esperado.longValue(), amarelos,
                "Quantidade de cartões amarelos não corresponde ao esperado para " + atleta);
    }

    @Então("o atleta deve ter {int} cartões vermelhos recebidos")
    public void validar_cartoes_vermelhos(Integer esperado) {
        long vermelhos = repository.buscarCartoesPorAtleta(atleta)
                .stream().filter(c -> "vermelho".equals(c.getTipo())).count();
        assertEquals(esperado.longValue(), vermelhos,
                "Quantidade de cartões vermelhos não corresponde ao esperado para " + atleta);
    }

    @Então("o atleta deve permanecer {string} para escalação")
    @Então("o atleta deve permanecer {string} para a próxima escalação")
    @Então("o atleta deve voltar a ficar {string} para escalação")
    public void validar_disponibilidade(String status) {
        boolean esperadoDisponivel = "disponível".equals(status);
        assertEquals(esperadoDisponivel, this.disponivel,
                "Status de disponibilidade não corresponde ao esperado para " + atleta);
    }

    @Então("o atleta deve ficar {string} para a próxima escalação")
    @Então("o atleta deve permanecer {string} por conta do cartão vermelho")
    public void validar_suspensao(String status) {
        boolean esperadoSuspenso = "suspenso".equals(status);
        assertEquals(esperadoSuspenso, this.suspenso,
                "Status de suspensão não corresponde ao esperado para " + atleta);
    }

    @Então("o atleta deve permanecer {string} por conta do contrato inativo")
    public void validar_indisponibilidade_contrato(String status) {
        boolean esperadoIndisponivel = "indisponível".equals(status);
        assertEquals(esperadoIndisponivel, !this.disponivel,
                "Status de indisponibilidade por contrato inativo não corresponde ao esperado para " + atleta);
    }
}