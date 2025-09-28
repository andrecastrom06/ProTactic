package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.capitao.CapitaoRepositoryFake;
import dev.com.protactic.dominio.principal.capitao.CapitaoService;

public class DefinicaoCapitaoFeature {

    private Jogador jogador;
    private CapitaoService service;
    private CapitaoRepositoryFake repository;

    @Dado("um jogador chamado {string}")
    public void um_jogador_chamado(String nome) {
        repository = new CapitaoRepositoryFake();
        service = new CapitaoService(repository);
        jogador = new Jogador(nome); // usa o construtor que você já tem
    }

    @E("ele possui contrato {string} com o {string}")
    public void ele_possui_contrato_com_o_clube(String statusContrato, String nomeClube) {
        Clube clube = new Clube(nomeClube);
        // cria Contrato com o clube e seta status (assumo que Contrato tem setStatus/getStatus)
        Contrato contrato = new Contrato(clube);
        // padroniza: se user passou "ativo"/"inativo" usamos isso
        if ("ativo".equalsIgnoreCase(statusContrato) || "ATIVO".equalsIgnoreCase(statusContrato)) {
            contrato.setStatus("ATIVO");
        } else {
            contrato.setStatus("INATIVO");
        }
        jogador.setClube(clube);
        jogador.setContrato(contrato);
    }

    @E("ele tem {string} de clube")
    public void ele_tem_de_clube(String tempo) {
        // entradas possíveis: "4 anos", "6 meses"
        int anos = 0;
        tempo = tempo.trim().toLowerCase();
        if (tempo.contains("ano")) {
            // pega número antes de 'ano(s)'
            try {
                anos = Integer.parseInt(tempo.split(" ")[0]);
            } catch (Exception e) {
                anos = 0;
            }
        } else if (tempo.contains("mes")) {
            // meses => menos de 1 ano -> anos = 0
            anos = 0;
        }
        jogador.setAnosDeClube(anos);
    }

    @E("sua minutagem é {string}")
    public void sua_minutagem_e(String minutagem) {
        jogador.setMinutagem(minutagem);
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void o_treinador_tenta_definir_como_capitao(String nome) {
        // chama o serviço; ele salva no repo se aprovado
        service.definirCapitao(jogador);
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void deve_ser_definido_como_capitao_do(String nome, String clube) {
        Jogador capitao = repository.buscarCapitaoPorClube(clube);
        assertNotNull(capitao, "Capitão deveria ter sido salvo no repositório");
        assertTrue(capitao.isCapitao(), "Flag de capitão deveria ser true");
        assertEquals(nome, capitao.getNome(), "Nome do capitão salvo não bate");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void nao_deve_ser_definido_como_capitao_do(String nome, String clube) {
        Jogador capitao = repository.buscarCapitaoPorClube(clube);
        // se houver algum capitão no repo, ele não pode ser esse nome; se não houver, também está OK
        if (capitao != null) {
            assertFalse(capitao.getNome().equals(nome) && capitao.isCapitao(),
                        "Jogador '" + nome + "' não deveria ser capitão do clube " + clube);
        } else {
            // nenhum capitão salvo -> ok
            assertNull(capitao);
        }
    }
}
