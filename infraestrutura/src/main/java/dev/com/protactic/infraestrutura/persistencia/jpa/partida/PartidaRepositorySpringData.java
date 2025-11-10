package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepositorySpringData extends JpaRepository<PartidaJPA, Integer> {
    
 
    List<PartidaResumo> findAllBy();

    List<PartidaResumo> findByClubeCasaId(Integer clubeCasaId);

    List<PartidaResumo> findByClubeVisitanteId(Integer clubeVisitanteId);
}