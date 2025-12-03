package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import java.util.List;

@Repository
public interface PremiacaoRepositorySpringData extends JpaRepository<PremiacaoJPA, Integer> {

    @Query("SELECT p FROM Premiacao p WHERE p.jogador.id = :jogadorId")
    List<PremiacaoResumo> findByJogadorId(@Param("jogadorId") Integer jogadorId);

    @Query("SELECT p FROM Premiacao p")
    List<PremiacaoResumo> findAllBy();

    List<PremiacaoResumo> findByNome(String nome);
}