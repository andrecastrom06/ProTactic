package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

// --- (INÍCIO DAS ADIÇÕES DE DEBUG) ---
import org.springframework.web.bind.annotation.GetMapping; // 1. Adiciona import
import dev.com.protactic.infraestrutura.persistencia.jpa.usuario.UsuarioJPA; // 2. Adiciona import
import dev.com.protactic.infraestrutura.persistencia.jpa.usuario.UsuarioRepositorySpringData; // 3. Adiciona import
import java.util.List; // 4. Adiciona import
// --- (FIM DAS ADIÇÕES DE DEBUG) ---

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.login.LoginService;

@RestController
@RequestMapping("backend/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginControlador {

    @Autowired
    private LoginService loginService;

    // --- (INÍCIO DAS ADIÇÕES DE DEBUG) ---
    // 5. Injeta o repositório JPA diretamente para debug
    @Autowired
    private UsuarioRepositorySpringData usuarioRepositoryJPA;
    // --- (FIM DAS ADIÇÕES DE DEBUG) ---


    /**
     * DTO (Formulário) para receber os dados de login.
     */
    public record LoginFormulario(
        String login,
        String senha
    ) {}

    /**
     * Endpoint para POST /backend/login
     */
    @PostMapping
    public ResponseEntity<Usuario> login(@RequestBody LoginFormulario formulario) {
        // ... (este método fica igual)
        try {
            Usuario usuario = loginService.autenticar(
                formulario.login(), 
                formulario.senha()
            );
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // --- (INÍCIO DAS ADIÇÕES DE DEBUG) ---
    /**
     * GET /backend/login/debug-users
     * Endpoint temporário para vermos se a tabela USUARIO foi populada.
     */
    @GetMapping("/debug-users")
    public List<UsuarioJPA> getTodosOsUsuarios() {
        // 6. Retorna todos os usuários diretamente do banco
        return usuarioRepositoryJPA.findAll();
    }
    // --- (FIM DAS ADIÇÕES DE DEBUG) ---
}