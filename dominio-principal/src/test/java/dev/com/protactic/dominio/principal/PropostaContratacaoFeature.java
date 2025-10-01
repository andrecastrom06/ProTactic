package dev.com.protactic.dominio.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.com.protactic.dominio.principal.proposta.PropostaRepository;
import dev.com.protactic.dominio.principal.proposta.PropostaService;
import dev.com.protactic.mocks.PropostaMock;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class PropostaContratacaoFeature {

    private Jogador jogador;
    private Clube clubeProponente;
    private Date dataAtual;
    private Proposta proposta;
    private Exception excecao;
    private PropostaRepository propostaRepository = new PropostaMock();
    private PropostaService propostaService = new PropostaService(propostaRepository);

    @Dado("um jogador chamado {string} que não tem contrato")
    public void um_jogador_chamado_que_não_tem_contrato(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador, null);
        this.jogador.setContrato(null);
    }

    @Dado("um jogador chamado {string} que tem contrato ativo com o {string}")
    public void um_jogador_chamado_que_tem_contrato_ativo_com_o(String nomeJogador, String clube) {
        Clube clubeObj = new Clube(clube);
        Contrato contrato = new Contrato(clubeObj);
        contrato.setStatus("ATIVO");
        this.jogador = new Jogador(nomeJogador, clubeObj);
        this.jogador.setContrato(contrato);
    }

    @Dado("um jogador chamado {string} que tem contrato com o {string}")
    public void um_jogador_chamado_que_tem_contrato_com_o(String nomeJogador, String clube) {
        Clube clubeObj = new Clube(clube);
        Contrato contrato = new Contrato(clubeObj);
        contrato.setStatus("ATIVO");
        this.jogador = new Jogador(nomeJogador, clubeObj);
        this.jogador.setContrato(contrato);
    }

    @Dado("a data é {string}")
    public void a_data_e(String data) throws ParseException {
        this.dataAtual = new SimpleDateFormat("dd/MM/yyyy").parse(data);
    }

    @Quando("um analista do {string} cria uma proposta de contrato para {string}")
    public void um_analista_do_cria_uma_proposta_de_contrato_para(String nomeClube, String nomeJogador) {
        this.clubeProponente = new Clube(nomeClube);
        try {
            this.proposta = propostaService.criarProposta(jogador, clubeProponente, dataAtual);
        } catch (Exception e) {
            this.excecao = e;
        }
    }

    @Então("a proposta deve ser registrada com sucesso")
    public void a_proposta_deve_ser_registrada_com_sucesso() {
        assertEquals(jogador, proposta.getJogador());
        assertEquals(clubeProponente, proposta.getPropositor());
        assertEquals(dataAtual, proposta.getData());
    }

    @Então("o sistema deve lançar uma exceção com a mensagem {string}")
    public void o_sistema_deve_lançar_uma_exceção_com_a_mensagem(String mensagemEsperada) {
        assertThrows(Exception.class, () -> { throw excecao; });
        assertEquals(mensagemEsperada, excecao.getMessage());
    }
}