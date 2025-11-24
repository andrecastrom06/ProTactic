package dev.com.protactic.apresentacao.principal.autenticacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping; 
import java.util.List; 

import dev.com.protactic.aplicacao.principal.usuario.UsuarioResumo;
import dev.com.protactic.aplicacao.principal.usuario.UsuarioServicoAplicacao;

import dev.com.protactic.dominio.principal.Usuario;
import dev.com.protactic.dominio.principal.login.LoginService;

@RestController
@RequestMapping("backend/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginControlador {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UsuarioServicoAplicacao usuarioServicoAplicacao;

    public record LoginFormulario(
        String login,
        String senha
    ) {}

    @PostMapping
    public ResponseEntity<Usuario> login(@RequestBody LoginFormulario formulario) {
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

    @GetMapping("/debug-users")
    public List<UsuarioResumo> getTodosOsUsuarios() {
        return usuarioServicoAplicacao.pesquisarResumos();
    }
  
}