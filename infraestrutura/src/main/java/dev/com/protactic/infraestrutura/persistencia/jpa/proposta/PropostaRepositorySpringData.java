package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepositorySpringData extends JpaRepository<PropostaJPA, Integer> {
    
 
    String PROPOSTA_RESUMO_QUERY = "SELECT " +
        " p.id as id, p.status as status, p.valor as valor, p.data as data, " +
        " j.id as jogadorId, j.nome as atletaNome, j.posicao as atletaPosicao, j.idade as atletaIdade, " +
        " c.nome as clubeAtualNome " + 
        " FROM Proposta p " +
        " JOIN Jogador j ON j.id = p.jogadorId " +
        " LEFT JOIN Clube c ON c.id = p.receptorId "; 


    @Query(PROPOSTA_RESUMO_QUERY)
    List<PropostaResumo> findPropostaResumos();

    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.propositorId = :propositorId")
    List<PropostaResumo> findPropostaResumosByPropositorId(@Param("propositorId") Integer propositorId);

    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.receptorId = :receptorId")
    List<PropostaResumo> findPropostaResumosByReceptorId(@Param("receptorId") Integer receptorId);

    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.jogadorId = :jogadorId")
    List<PropostaResumo> findPropostaResumosByJogadorId(@Param("jogadorId") Integer jogadorId);
    
}