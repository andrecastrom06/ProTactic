package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;
import dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao.EscalacaoControlador.EscalacaoFormulario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Encapsula a lógica de registrar um jogador em uma escalação.
 */
public class RegistrarEscalacaoComando implements ComandoEscalacao {

    private final DefinirEsquemaTaticoService definirEsquemaTaticoService;
    private final EscalacaoFormulario formulario;

    public RegistrarEscalacaoComando(
            DefinirEsquemaTaticoService definirEsquemaTaticoService,
            EscalacaoFormulario formulario) {
        this.definirEsquemaTaticoService = definirEsquemaTaticoService;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        if (formulario == null || formulario.jogoData() == null || formulario.nomeJogador() == null) {
            return ResponseEntity.badRequest().body("O formulário (jogoData, nomeJogador) não pode ser nulo.");
        }
        
        try {
            // Lógica de negócio do Controller movida para o Command
            boolean sucesso = definirEsquemaTaticoService.registrarJogadorEmEscalacao(
                formulario.jogoData(),
                formulario.nomeJogador(),
                formulario.clubeId()
            );

            if (sucesso) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Jogador inapto para a partida (lesionado, suspenso ou sem contrato).");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}