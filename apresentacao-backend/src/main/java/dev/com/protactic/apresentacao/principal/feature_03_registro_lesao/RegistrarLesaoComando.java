package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;
import dev.com.protactic.apresentacao.principal.feature_03_registro_lesao.RegistroDeLesaoControlador.RegistrarLesaoFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RegistrarLesaoComando implements ComandoRegistroLesao {

    private final RegistroLesoesServico registroLesoesServico;
    private final Integer jogadorId;
    private final RegistrarLesaoFormulario formulario;

    public RegistrarLesaoComando(
            RegistroLesoesServico registroLesoesServico,
            Integer jogadorId,
            RegistrarLesaoFormulario formulario) {
        this.registroLesoesServico = registroLesoesServico;
        this.jogadorId = jogadorId;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        try {
            registroLesoesServico.registrarLesaoPorId(jogadorId, formulario.grau());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}