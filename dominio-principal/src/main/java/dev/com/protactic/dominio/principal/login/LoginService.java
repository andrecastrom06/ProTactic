package dev.com.protactic.dominio.principal.login;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import java.util.Optional;
// (Remove o import do JogadorRepository, não é necessário aqui)

public class LoginService {

    private final UsuarioRepository usuarioRepository; 
    private final ClubeRepository clubeRepository; // Apenas este é necessário

    public LoginService(UsuarioRepository usuarioRepository,
                        // JogadorRepository jogadorRepository, // Remove
                        ClubeRepository clubeRepository) {
        this.usuarioRepository = usuarioRepository;
        // this.jogadorRepository = jogadorRepository; // Remove
        this.clubeRepository = clubeRepository;
    }

    public Usuario autenticar(String login, String senha) throws Exception {
        
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorLoginESenha(login, senha);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Login ou senha incorretos.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha("[SENHA_OCULTA]");
        
        // --- (INÍCIO DA CORREÇÃO) ---
        // Lógica Bónus: Encontrar o Clube deste usuário
        // O Spring Data não suporta 'find by id_treinador OR id_analista...' facilmente.
        // Vamos fazer da forma mais simples: carregar todos os clubes e filtrar.
        
        final int usuarioId = usuario.getId();
        Optional<Clube> clubeDoUsuario = clubeRepository.listarTodos().stream()
            .filter(clube -> 
                   Integer.valueOf(usuarioId).equals(clube.getTreinadorId())
                || Integer.valueOf(usuarioId).equals(clube.getAnalistaId())
                || Integer.valueOf(usuarioId).equals(clube.getPreparadorId())
            )
            .findFirst();

        // Se encontrámos um clube, adicionamos o ID ao objeto Usuario
        if (clubeDoUsuario.isPresent()) {
            usuario.setClubeId(clubeDoUsuario.get().getId()); // <-- DESCOMENTA/ADICIONA ISTO
        }
        return usuario;
    }
}