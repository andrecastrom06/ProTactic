package dev.com.protactic.infraestrutura.persistencia.jpa.usuario;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.UsuarioRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.List;

import dev.com.protactic.aplicacao.principal.usuario.UsuarioRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.usuario.UsuarioResumo;

@Component 
public class UsuarioRepositoryImpl implements UsuarioRepository, UsuarioRepositorioAplicacao {

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

    @Override
    public List<UsuarioResumo> pesquisarResumos() {
        return repositoryJPA.findAllResumosBy();
    }
}