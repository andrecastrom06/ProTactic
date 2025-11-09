package dev.com.protactic.aplicacao.principal.partida;

import java.util.Date;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade Partida.
 * Contém apenas os dados necessários para listagens.
 */
public interface PartidaResumo {
    
    int getId();
    Integer getClubeCasaId();
    Integer getClubeVisitanteId();
    Date getDataJogo();
    Date getHora();
    int getPlacarClubeCasa();
    int getPlacarClubeVisitante();

}