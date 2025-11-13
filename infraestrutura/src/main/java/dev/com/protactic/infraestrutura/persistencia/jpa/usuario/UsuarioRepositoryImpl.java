package dev.com.protactic.infraestrutura.persistencia.jpa.usuario;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.login.UsuarioRepository; // A interface do Domínio
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador; // O teu ModelMapper
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Implementação (Infra) da interface UsuarioRepository.
 * Esta classe "fala" com o Spring Data (JPA).
 */
@Component // Marca como um Bean do Spring
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    // Injeta o Spring Data e o Mapeador
    public UsuarioRepositoryImpl(UsuarioRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    @Override
    public Optional<Usuario> buscarPorLoginESenha(String login, String senha) {
        // 1. Busca o objeto JPA do banco
        Optional<UsuarioJPA> jpaOpt = repositoryJPA.findByLoginAndSenha(login, senha);
        
        // 2. Converte de UsuarioJPA para a entidade de domínio Usuario
        return jpaOpt.map(jpa -> mapeador.map(jpa, Usuario.class));
    }
}