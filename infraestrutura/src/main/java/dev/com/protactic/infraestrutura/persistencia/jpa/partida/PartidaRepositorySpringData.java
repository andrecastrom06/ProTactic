package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

// 1. IMPORTAR O RESUMO E O LIST
import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepositorySpringData extends JpaRepository<PartidaJPA, Integer> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * Projeta todas as Partidas para a interface PartidaResumo.
     */
    List<PartidaResumo> findAllBy();

    /**
     * Filtra por 'clubeCasaId' e projeta para PartidaResumo.
     */
    List<PartidaResumo> findByClubeCasaId(Integer clubeCasaId);

    /**
     * Filtra por 'clubeVisitanteId' e projeta para PartidaResumo.
     */
    List<PartidaResumo> findByClubeVisitanteId(Integer clubeVisitanteId);
}