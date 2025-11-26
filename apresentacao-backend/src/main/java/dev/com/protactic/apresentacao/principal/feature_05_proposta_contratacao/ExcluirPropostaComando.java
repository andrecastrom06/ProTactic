package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Encapsula a lógica de exclusão de uma Proposta.
 */
public class ExcluirPropostaComando implements ComandoProposta {

    private final PropostaService propostaService;
    private final Integer propostaId;

    public ExcluirPropostaComando(PropostaService propostaService, Integer propostaId) {
        this.propostaService = propostaService;
        this.propostaId = propostaId;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            propostaService.excluirProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir proposta: " + e.getMessage());
        }
    }
}