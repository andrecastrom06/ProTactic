package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import dev.com.protactic.aplicacao.principal.nota.NotaResumo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepositorySpringData extends JpaRepository<NotaJPA, NotaPK> {

    List<NotaResumo> findByJogoId(String jogoId);

    List<NotaResumo> findByJogadorId(String jogadorId);


@Query(value = """
        SELECT n.jogadorId
        FROM Nota n 
        JOIN Partida p ON CAST(SUBSTRING(n.jogoId, 6) AS integer) = p.id 
        WHERE FUNCTION('MONTH', p.dataJogo) = FUNCTION('MONTH', :data) 
        AND FUNCTION('YEAR', p.dataJogo) = FUNCTION('YEAR', :data) 
        GROUP BY n.jogadorId
        ORDER BY AVG(n.nota) DESC 
        LIMIT 1
    """)
    Optional<Integer> findJogadorComMelhorNotaNoMes(@Param("data") Date data);
}