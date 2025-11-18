package dev.com.protactic.aplicacao.principal.partida;

import java.util.Date;


public interface PartidaResumo {
    
    int getId();
    Integer getClubeCasaId();
    Integer getClubeVisitanteId();
    Date getDataJogo();
    String getHora();
    int getPlacarClubeCasa();
    int getPlacarClubeVisitante();

}