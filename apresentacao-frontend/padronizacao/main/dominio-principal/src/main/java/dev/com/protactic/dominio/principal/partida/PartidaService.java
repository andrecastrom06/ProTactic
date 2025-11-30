package dev.com.protactic.dominio.principal.partida; // Ou .treinoTatico se preferires, mas idealmente fica num pacote partida

import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

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

    public record DadosDesempenhoAtleta(Integer atletaId, int gols, int assistencias) {}
}