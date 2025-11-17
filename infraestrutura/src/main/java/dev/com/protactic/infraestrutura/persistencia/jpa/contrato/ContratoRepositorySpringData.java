package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- 1. Importa o @Query
import org.springframework.data.repository.query.Param; // <-- 2. Importa o @Param
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContratoRepositorySpringData extends JpaRepository<ContratoJPA, Integer> {

  
    String CONTRATO_RESUMO_QUERY = "SELECT " +
        " c.id as id, c.duracaoMeses as duracaoMeses, c.salario as salario, c.status as status, c.clubeId as clubeId, " +
        " j.nome as atletaNome, j.posicao as atletaPosicao, j.idade as atletaIdade, j.chegadaNoClube as dataInicio " +
        " FROM Contrato c JOIN Jogador j ON j.contratoId = c.id "; // JOIN entre Contrato e Jogador
    @Query(CONTRATO_RESUMO_QUERY)
    List<ContratoResumo> findContratoResumos();

    @Query(CONTRATO_RESUMO_QUERY + " WHERE c.clubeId = :clubeId")
    List<ContratoResumo> findContratoResumosByClubeId(@Param("clubeId") Integer clubeId);
    
}