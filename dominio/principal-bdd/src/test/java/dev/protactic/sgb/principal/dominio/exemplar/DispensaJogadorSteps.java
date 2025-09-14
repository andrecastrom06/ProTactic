package dev.protactic.sgb.principal.dominio.exemplar;

import dev.protactic.sgb.principal.dominio.Jogador;
import dev.protactic.sgb.principal.dominio.Clube;
import dev.protactic.sgb.principal.dominio.JogadorService;
import dev.protactic.sgb.principal.dominio.ClubeRepository;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;

public class DispensaJogadorSteps {

    @Autowired
    private JogadorService jogadorService;

    @Autowired
    private ClubeRepository clubeRepository;

    private Clube clube;
    private Jogador jogador;
    private Runnable acaoDispensarJogador;

    @Dado("que o clube {string} já possui o jogador {string}")
    public void queOTimeJaPossuiOJogador(String nomeClube, String nomeJogador) {
        clube = new Clube(nomeClube);
        jogador = new Jogador(nomeJogador, clube);
        clube.adicionarJogador(jogador);
        clubeRepository.salvar(clube);
    }

    @Quando("o gestor do clube tenta dispensar o jogador {string}")
    public void oGestorDoClubeTentaDispensarOJogador(String nomeJogador) {
        this.acaoDispensarJogador = () -> {
            try {
                jogadorService.dispensarJogador(nomeJogador);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Quando("o gestor do clube tenta dispensar o jogador que não pertence ao clube")
    public void oGestorDoClubeTentaDispensarOJogadorQueNaoPertenceAoClube() {
        acaoDispensarJogador = () -> {
            try {
                jogadorService.dispensarJogador("Jogador Inexistente");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Então("o jogador {string} é dispensado do clube {string}")
    public void oJogadorEDispensadoDoClube(String nomeJogador, String nomeClube) {
        Clube clubeAtualizado = clubeRepository.buscarPorNome(nomeClube);
        Assertions.assertFalse(clubeAtualizado.possuiJogador(nomeJogador), "O jogador não foi dispensado do clube.");
    }
    
    @Então("o sistema deve retornar uma exceção de regra de negócio")
    public void oSistemaDeveRetornarUmaExcecaoDeRegraDeNegocio() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            acaoDispensarJogador.run();
        });
    }

}