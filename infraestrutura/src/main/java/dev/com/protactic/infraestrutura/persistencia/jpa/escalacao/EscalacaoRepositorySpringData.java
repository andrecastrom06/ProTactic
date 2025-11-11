package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalacaoRepositorySpringData extends JpaRepository<EscalacaoJPA, Integer> {
    
    // O JpaRepository já nos dá o método .save() que precisamos
}