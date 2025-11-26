package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao.PropostaControlador.PropostaValorFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Encapsula a lógica de edição do valor de uma Proposta.
 */
public class EditarValorPropostaComando implements ComandoProposta {

    private final PropostaService propostaService;
    private final Integer propostaId;
    private final PropostaValorFormulario formulario;

    public EditarValorPropostaComando(
            PropostaService propostaService, 
            Integer propostaId, 
            PropostaValorFormulario formulario) {
        this.propostaService = propostaService;
        this.propostaId = propostaId;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            propostaService.editarValorProposta(propostaId, formulario.novoValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Trata exceções do domínio (ex: proposta não encontrada ou inválida)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar proposta: " + e.getMessage());
        }
    }
}