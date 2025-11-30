package dev.com.protactic.dominio.principal.login;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
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