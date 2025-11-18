package dev.com.protactic.dominio.principal.partida; // Ou .treinoTatico se preferires, mas idealmente fica num pacote partida

import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import java.util.Date;

public class PartidaService {
    
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;

    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
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
}