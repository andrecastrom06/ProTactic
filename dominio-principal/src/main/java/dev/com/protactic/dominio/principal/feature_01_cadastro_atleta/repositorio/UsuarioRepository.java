package dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio;

import java.util.Optional;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;

public interface UsuarioRepository {
    
    // Método existente
    Optional<Usuario> buscarPorLoginESenha(String login, String senha);

    // NOVO MÉTODO (Adicione esta linha para corrigir o erro no Controlador)
    Usuario buscarPorId(Integer id);
}