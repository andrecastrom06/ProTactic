package dev.com.protactic.dominio.principal.login;

import dev.com.protactic.dominio.principal.Usuario;
import java.util.Optional;

/**
 * Interface de Domínio (o "Contrato") para buscar Usuários.
 * O LoginService vai depender disto.
 */
public interface UsuarioRepository {
    
    /**
     * Busca um usuário pelo login e senha.
     * @return Um Optional contendo a entidade de domínio Usuario.
     */
    Optional<Usuario> buscarPorLoginESenha(String login, String senha);
}