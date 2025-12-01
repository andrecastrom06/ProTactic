package dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.UsuarioRepository;

import java.util.Optional;

public class LoginService {

    private final UsuarioRepository usuarioRepository; 
    private final ClubeRepository clubeRepository; 

    public LoginService(UsuarioRepository usuarioRepository,
                        ClubeRepository clubeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clubeRepository = clubeRepository;
    }

    public Usuario autenticar(String login, String senha) throws Exception {
        
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorLoginESenha(login, senha);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Login ou senha incorretos.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha("[SENHA_OCULTA]");
        
     
        final int usuarioId = usuario.getId();
        Optional<Clube> clubeDoUsuario = clubeRepository.listarTodos().stream()
            .filter(clube -> 
                   Integer.valueOf(usuarioId).equals(clube.getTreinadorId())
                || Integer.valueOf(usuarioId).equals(clube.getAnalistaId())
                || Integer.valueOf(usuarioId).equals(clube.getPreparadorId())
            )
            .findFirst();

        if (clubeDoUsuario.isPresent()) {
            usuario.setClubeId(clubeDoUsuario.get().getId()); 
        }
        return usuario;
    }
}