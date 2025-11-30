package dev.com.protactic.aplicacao.principal.partida;

import java.util.List;
import org.springframework.stereotype.Service;


@Service 
public class PartidaServicoAplicacao {
    
    private final PartidaRepositorioAplicacao repositorio;

    public PartidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<PartidaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }


    public List<PartidaResumo> pesquisarResumosPorClubeCasa(Integer clubeCasaId) {
        return repositorio.pesquisarResumosPorClubeCasa(clubeCasaId);
    }

    public List<PartidaResumo> pesquisarResumosPorClubeVisitante(Integer clubeVisitanteId) {
        return repositorio.pesquisarResumosPorClubeVisitante(clubeVisitanteId);
    }
}