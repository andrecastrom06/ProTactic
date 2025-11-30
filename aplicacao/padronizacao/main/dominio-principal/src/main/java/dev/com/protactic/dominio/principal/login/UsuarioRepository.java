package dev.com.protactic.dominio.principal.login;

import dev.com.protactic.dominio.principal.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    
 
    Optional<Usuario> buscarPorLoginESenha(String login, String senha);
}