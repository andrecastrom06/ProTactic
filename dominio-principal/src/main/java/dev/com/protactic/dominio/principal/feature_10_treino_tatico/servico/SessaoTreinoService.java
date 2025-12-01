package dev.com.protactic.dominio.principal.feature_10_treino_tatico.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade.SessaoTreino;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.repositorio.SessaoTreinoRepository;

import java.util.List;
import java.util.ArrayList;

public class SessaoTreinoService {
    private final SessaoTreinoRepository repository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;

    public SessaoTreinoService(SessaoTreinoRepository repository,
                               PartidaRepository partidaRepository,
                               JogadorRepository jogadorRepository) {
        this.repository = repository;
        this.partidaRepository = partidaRepository;
        this.jogadorRepository = jogadorRepository;
    }

    public SessaoTreino criarSessaoPorIds(String nome, Integer partidaId, List<Integer> convocadosIds, Integer clubeId) throws Exception {
        
        Partida partida = null;

        if (partidaId != null) {
            partida = partidaRepository.buscarPorId(partidaId)
                    .orElseThrow(() -> new Exception("Partida não encontrada: " + partidaId));
        }
    
        List<Jogador> jogadores = new ArrayList<>();
        if (convocadosIds != null) {
            for (Integer jogadorId : convocadosIds) {
                Jogador j = jogadorRepository.buscarPorId(jogadorId);
                if (j != null) {
                    jogadores.add(j);
                } else {
                     System.err.println("Aviso: Jogador com ID " + jogadorId + " não encontrado.");
                }
            }
        }
        
        return this.criarSessao(nome, partida, jogadores, clubeId);
    }

    public SessaoTreino criarSessao(String nome, Partida partida, List<Jogador> jogadores, Integer clubeId) {
        
        if (partida == null) {
            throw new IllegalArgumentException("Não há jogo no calendário para vincular este treino.");
        }

        SessaoTreino sessao = new SessaoTreino(nome, partida, clubeId);

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