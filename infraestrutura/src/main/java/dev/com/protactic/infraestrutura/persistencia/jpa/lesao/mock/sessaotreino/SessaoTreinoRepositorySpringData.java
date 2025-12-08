package dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino;

import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoTreinoRepositorySpringData extends JpaRepository<SessaoTreinoJPA, Integer> {
    

    @Query("SELECT s FROM SessaoTreino s " +
           "JOIN Partida p ON p.id = s.partidaId " +
           "JOIN Clube cc ON cc.id = p.clubeCasaId " +
           "JOIN Clube cv ON cv.id = p.clubeVisitanteId " +
           "WHERE CONCAT(cc.nome, ' vs ', cv.nome) = :partidaNome")
    List<SessaoTreinoJPA> findByPartidaNome(@Param("partidaNome") String partidaNome);

    List<SessaoTreinoResumo> findAllBy();

  
    List<SessaoTreinoResumo> findByPartidaId(Integer partidaId);

    List<SessaoTreinoResumo> findByConvocadosIdsContaining(Integer jogadorId);

    List<SessaoTreinoJPA> findByPartidaIdAndClubeId(Integer partidaId, Integer clubeId);
    
}