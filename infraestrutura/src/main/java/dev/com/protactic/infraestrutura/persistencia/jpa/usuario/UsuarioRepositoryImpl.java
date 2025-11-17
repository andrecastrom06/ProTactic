package dev.com.protactic.infraestrutura.persistencia.jpa.usuario;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.login.UsuarioRepository; // A interface do Domínio
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador; // O teu ModelMapper
import org.springframework.stereotype.Component;
import java.util.Optional;


@Component 
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    
    public UsuarioRepositoryImpl(UsuarioRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    @Override
    public Optional<Usuario> buscarPorLoginESenha(String login, String senha) {
        Optional<UsuarioJPA> jpaOpt = repositoryJPA.findByLoginAndSenha(login, senha);
        
        return jpaOpt.map(jpa -> mapeador.map(jpa, Usuario.class));
    }
}