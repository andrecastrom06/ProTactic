package dev.com.protactic.infraestrutura.persistencia.jpa.suspensao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SuspensaoRepositorySpringData extends JpaRepository<SuspensaoJPA, Integer> {
    
    Optional<SuspensaoJPA> findByIdJogador(Integer idJogador);
}