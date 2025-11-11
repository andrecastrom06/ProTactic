package dev.com.protactic.infraestrutura.persistencia.jpa.suspensao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SuspensaoRepositorySpringData extends JpaRepository<SuspensaoJPA, Integer> {
    
    // Busca um registro de suspensão pelo ID do jogador
    Optional<SuspensaoJPA> findByIdJogador(Integer idJogador);
}