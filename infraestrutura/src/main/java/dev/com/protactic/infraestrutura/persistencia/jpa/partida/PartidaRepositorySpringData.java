package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepositorySpringData extends JpaRepository<PartidaJPA, Integer> {
    
    List<PartidaResumo> findAllBy();
    List<PartidaResumo> findByClubeCasaId(Integer clubeCasaId);
    List<PartidaResumo> findByClubeVisitanteId(Integer clubeVisitanteId);
    @Query("SELECT p FROM Partida p WHERE (p.clubeCasaId = :clubeId OR p.clubeVisitanteId = :clubeId) AND MONTH(p.dataJogo) = :mes AND YEAR(p.dataJogo) = :ano")
    List<PartidaJPA> findByClubeAndMes(@Param("clubeId") Integer clubeId, @Param("mes") int mes, @Param("ano") int ano);
}