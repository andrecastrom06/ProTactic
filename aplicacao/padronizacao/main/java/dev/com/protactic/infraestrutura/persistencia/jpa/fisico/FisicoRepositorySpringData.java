package dev.com.protactic.infraestrutura.persistencia.jpa.fisico;

import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FisicoRepositorySpringData extends JpaRepository<FisicoJPA, Integer> {

    List<FisicoJPA> findByJogadorId(Integer jogadorId);

    
    List<FisicoResumo> findAllByJogadorId(Integer jogadorId);
}