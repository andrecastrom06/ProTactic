package dev.com.protactic.aplicacao.principal.partida;

import java.util.Date;


public interface PartidaResumo {
    
    int getId();
    Integer getClubeCasaId();
    Integer getClubeVisitanteId();
    Date getDataJogo();
    Date getHora();
    int getPlacarClubeCasa();
    int getPlacarClubeVisitante();

}