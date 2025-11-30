package dev.com.protactic.infraestrutura.persistencia.jpa.usuario;

import dev.com.protactic.aplicacao.principal.usuario.UsuarioResumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepositorySpringData extends JpaRepository<UsuarioJPA, Integer> {
    
    Optional<UsuarioJPA> findByLoginAndSenha(String login, String senha);
    
    List<UsuarioResumo> findAllResumosBy();
}