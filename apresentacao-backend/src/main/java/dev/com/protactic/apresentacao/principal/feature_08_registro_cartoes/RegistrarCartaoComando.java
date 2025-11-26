package dev.com.protactic.apresentacao.principal.feature_08_registro_cartoes;

import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
import dev.com.protactic.apresentacao.principal.feature_08_registro_cartoes.RegistroCartoesControlador.CartaoFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Encapsula a lógica de registrar um Cartão.
 */
public class RegistrarCartaoComando implements ComandoRegistroCartao {

    private final RegistroCartoesService registroCartoesService;
    private final CartaoFormulario formulario;

    public RegistrarCartaoComando(RegistroCartoesService registroCartoesService, CartaoFormulario formulario) {
        this.registroCartoesService = registroCartoesService;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        // Validação movida para o Command
        if (formulario == null) {
            return ResponseEntity.badRequest().body("O corpo da requisição não pode ser nulo.");
        }
        
        try {
            // Chamada ao Receiver (registroCartoesService)
            registroCartoesService.registrarCartao(formulario.atleta(), formulario.tipo());
            
            // Retorna 200 OK
            return ResponseEntity.ok().build(); 
            
        } catch (Exception e) {
            // Tratamento de exceção movido para o Command (substituindo o relançamento de RuntimeException)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar cartão: " + e.getMessage());
        }
    }
}