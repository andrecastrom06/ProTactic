package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EscalacaoRepositorySpringData extends JpaRepository<EscalacaoJPA, Integer> {
    
    Optional<EscalacaoJPA> findByPartidaIdAndClubeId(Integer partidaId, Integer clubeId);

    Optional<EscalacaoJPA> findByPartidaId(Integer partidaId);

    Optional<FormacaoResumo> findResumoById(Integer id);

    Optional<FormacaoResumo> findResumoByPartidaId(Integer partidaId);
}