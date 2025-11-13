package dev.com.protactic.infraestrutura.persistencia.jpa.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepositorySpringData extends JpaRepository<UsuarioJPA, Integer> {
    
    /**
     * O Spring Data JPA vai criar automaticamente a query:
     * "SELECT * FROM Usuario WHERE login = ? AND senha = ?"
     */
    Optional<UsuarioJPA> findByLoginAndSenha(String login, String senha);
}