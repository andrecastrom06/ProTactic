package dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.servico; // Ou .treinoTatico se preferires, mas idealmente fica num pacote partida

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;

import java.util.Date;
import java.util.List;

public class PartidaService {
    
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final JogadorRepository jogadorRepository; // Nova dependência

    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository, JogadorRepository jogadorRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.jogadorRepository = jogadorRepository;
    }

    public Partida criarPartida(Integer clubeCasaId, Integer clubeVisitanteId, Date dataJogo, String hora) throws Exception {
        Clube casa = clubeRepository.buscarPorId(clubeCasaId);
        Clube visitante = clubeRepository.buscarPorId(clubeVisitanteId);

        if (casa == null || visitante == null) {
            throw new Exception("Clube não encontrado.");
        }

        Partida partida = new Partida();
        partida.setClubeCasa(casa);
        partida.setClubeVisitante(visitante);
        partida.setDataJogo(dataJogo);
        partida.setHora(hora);
        
        partida.setPlacarClubeCasa(0);
        partida.setPlacarClubeVisitante(0);

        return partidaRepository.salvar(partida);
    }
    public void registrarEstatisticas(List<DadosDesempenhoAtleta> dados) {
        for (DadosDesempenhoAtleta dado : dados) {
            Jogador jogador = jogadorRepository.buscarPorId(dado.atletaId());
            
            if (jogador != null) {
                jogador.setJogos(jogador.getJogos() + 1);
                jogador.setGols(jogador.getGols() + dado.gols());
                jogador.setAssistencias(jogador.getAssistencias() + dado.assistencias());
                
                jogadorRepository.salvar(jogador);
            }
        }
    }
    public void atualizarPlacar(Integer partidaId, int golsCasa, int golsVisitante) throws Exception {
        Partida partida = partidaRepository.buscarPorId(partidaId)
                .orElseThrow(() -> new Exception("Partida não encontrada"));

        partida.setPlacarClubeCasa(golsCasa);
        partida.setPlacarClubeVisitante(golsVisitante);

        partidaRepository.salvar(partida);
    }

    public record DadosDesempenhoAtleta(Integer atletaId, int gols, int assistencias) {}
}