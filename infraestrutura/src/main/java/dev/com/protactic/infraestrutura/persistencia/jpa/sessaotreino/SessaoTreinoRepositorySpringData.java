package dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoTreinoRepositorySpringData extends JpaRepository<SessaoTreinoJPA, Integer> {
    
    // --- Método para o Repositório do DOMÍNIO ---
    
    // 1. A query complexa que o Domínio pediu (retorna Entidade JPA)
    @Query("SELECT s FROM SessaoTreino s " +
           "JOIN Partida p ON p.id = s.partidaId " +
           "JOIN Clube cc ON cc.id = p.clubeCasaId " +
           "JOIN Clube cv ON cv.id = p.clubeVisitanteId " +
           "WHERE CONCAT(cc.nome, ' vs ', cv.nome) = :partidaNome")
    List<SessaoTreinoJPA> findByPartidaNome(@Param("partidaNome") String partidaNome);


    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * 2. Projeta todas as Sessões para a interface SessaoTreinoResumo.
     */
    List<SessaoTreinoResumo> findAllBy();

    /**
     * 3. Filtra por 'partidaId' (simples) e projeta para SessaoTreinoResumo.
     */
    List<SessaoTreinoResumo> findByPartidaId(Integer partidaId);

    /**
     * 4. Filtra pela lista de 'convocadosIds' (ElementCollection)
     * e projeta para SessaoTreinoResumo.
     */
    List<SessaoTreinoResumo> findByConvocadosIdsContaining(Integer jogadorId);
}