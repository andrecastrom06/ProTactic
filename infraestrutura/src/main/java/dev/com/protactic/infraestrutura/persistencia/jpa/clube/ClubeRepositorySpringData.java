package dev.com.protactic.infraestrutura.persistencia.jpa.clube;

import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List; 

@Repository
public interface ClubeRepositorySpringData extends JpaRepository<ClubeJPA, Integer> {
    
    
    Optional<ClubeJPA> findByNome(String nome);

    List<ClubeResumo> findAllBy();

    List<ClubeResumo> findByCompeticaoId(Integer competicaoId);
}