package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalacaoSimplesRepositorySpringData extends JpaRepository<EscalacaoSimplesJPA, Integer> {
        
    List<EscalacaoSimplesJPA> findByJogoData(String jogoData);

    List<EscalacaoResumo> findAllByJogoData(String jogoData);
}