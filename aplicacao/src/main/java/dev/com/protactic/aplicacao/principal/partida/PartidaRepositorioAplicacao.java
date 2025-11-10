package dev.com.protactic.aplicacao.principal.partida;

import java.util.List;


public interface PartidaRepositorioAplicacao {
    
    
    List<PartidaResumo> pesquisarResumos();

    List<PartidaResumo> pesquisarResumosPorClubeCasa(Integer clubeCasaId);

    List<PartidaResumo> pesquisarResumosPorClubeVisitante(Integer clubeVisitanteId);
}