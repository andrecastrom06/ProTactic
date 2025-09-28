package dev.com.protactic.dominio.principal.treinoTatico;

import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Jogador;
import java.util.List;

public class SessaoTreinoService {
    private final SessaoTreinoRepository repository;

    public SessaoTreinoService(SessaoTreinoRepository repository) {
        this.repository = repository;
    }

    public SessaoTreino criarSessao(String nome, Partida partida, List<Jogador> jogadores) {
        if (partida == null) {
            throw new IllegalArgumentException("Não há jogo no calendário para vincular este treino.");
        }

        SessaoTreino sessao = new SessaoTreino(nome, partida);

        if (jogadores != null) {
            jogadores.forEach(sessao::adicionarConvocado);
        }

        repository.salvar(sessao);
        return sessao;
    }

    public List<SessaoTreino> listarSessoesPorPartida(String partidaNome) {
        return repository.listarPorPartida(partidaNome);
    }
}
