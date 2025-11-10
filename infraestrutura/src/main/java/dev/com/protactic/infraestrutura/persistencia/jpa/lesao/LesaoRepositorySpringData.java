package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LesaoRepositorySpringData extends JpaRepository<LesaoJPA, Integer> {

    List<LesaoJPA> findByJogadorId(Integer jogadorId);

    @Query("SELECT l FROM Lesao l WHERE l.jogadorId = :jogadorId AND l.lesionado = true")
    Optional<LesaoJPA> findAtivaByJogadorId(@Param("jogadorId") Integer jogadorId);

    List<LesaoResumo> findAllBy();

    List<LesaoResumo> findAllByJogadorId(Integer jogadorId);

    @Query("SELECT l FROM Lesao l WHERE l.jogadorId = :jogadorId AND l.lesionado = true")
    Optional<LesaoResumo> findResumoAtivoByJogadorId(@Param("jogadorId") Integer jogadorId);
}