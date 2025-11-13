package dev.com.protactic.dominio.principal.login;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import java.util.Optional;

public class LoginService {

    private final UsuarioRepository usuarioRepository; 
    private final JogadorRepository jogadorRepository;
    private final ClubeRepository clubeRepository;
    public LoginService(UsuarioRepository usuarioRepository,
                        JogadorRepository jogadorRepository,
                        ClubeRepository clubeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.jogadorRepository = jogadorRepository;
        this.clubeRepository = clubeRepository;
    }

    public Usuario autenticar(String login, String senha) throws Exception {
        
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorLoginESenha(login, senha);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Login ou senha incorretos.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha("[SENHA_OCULTA]");
 
        return usuario;
    }
}