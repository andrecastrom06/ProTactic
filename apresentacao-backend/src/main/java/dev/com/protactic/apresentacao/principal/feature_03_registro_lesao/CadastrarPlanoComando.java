package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;
import dev.com.protactic.apresentacao.principal.feature_03_registro_lesao.RegistroDeLesaoControlador.PlanoRecuperacaoFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CadastrarPlanoComando implements ComandoRegistroLesao {

    private final RegistroLesoesServico registroLesoesServico;
    private final Integer jogadorId;
    private final PlanoRecuperacaoFormulario formulario;

    public CadastrarPlanoComando(
            RegistroLesoesServico registroLesoesServico,
            Integer jogadorId,
            PlanoRecuperacaoFormulario formulario) {
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
            registroLesoesServico.cadastrarPlanoRecuperacaoPorId(
                jogadorId, 
                formulario.tempo(), 
                formulario.plano()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}