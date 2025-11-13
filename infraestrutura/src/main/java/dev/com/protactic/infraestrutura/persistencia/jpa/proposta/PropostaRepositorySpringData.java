package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- Importa o @Query
import org.springframework.data.repository.query.Param; // <-- Importa o @Param
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepositorySpringData extends JpaRepository<PropostaJPA, Integer> {
    
    /**
     * Query customizada que faz JOIN com Jogador e Clube
     * para buscar os nomes, em vez de só os IDs.
     */
    String PROPOSTA_RESUMO_QUERY = "SELECT " +
        " p.id as id, p.status as status, p.valor as valor, p.data as data, " +
        " j.id as jogadorId, j.nome as atletaNome, j.posicao as atletaPosicao, j.idade as atletaIdade, " +
        " c.nome as clubeAtualNome " + // c.nome é o "clube atual" (receptor)
        " FROM Proposta p " +
        " JOIN Jogador j ON j.id = p.jogadorId " +
        " LEFT JOIN Clube c ON c.id = p.receptorId "; // LEFT JOIN caso o jogador seja "Livre"

    // --- (INÍCIO DAS CORREÇÕES) ---

    // Substitui o antigo 'findAllBy()'
    @Query(PROPOSTA_RESUMO_QUERY)
    List<PropostaResumo> findPropostaResumos();

    // Substitui o antigo 'findByPropositorId()'
    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.propositorId = :propositorId")
    List<PropostaResumo> findPropostaResumosByPropositorId(@Param("propositorId") Integer propositorId);

    // Substitui o antigo 'findByReceptorId()'
    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.receptorId = :receptorId")
    List<PropostaResumo> findPropostaResumosByReceptorId(@Param("receptorId") Integer receptorId);

    // Substitui o antigo 'findByJogadorId()'
    @Query(PROPOSTA_RESUMO_QUERY + " WHERE p.jogadorId = :jogadorId")
    List<PropostaResumo> findPropostaResumosByJogadorId(@Param("jogadorId") Integer jogadorId);
    
    // --- (FIM DAS CORREÇÕES) ---
}