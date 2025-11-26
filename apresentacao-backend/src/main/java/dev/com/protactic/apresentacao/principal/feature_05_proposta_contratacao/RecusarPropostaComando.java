package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Encapsula a lógica de recusa de uma Proposta.
 */
public class RecusarPropostaComando implements ComandoProposta {

    private final PropostaService propostaService;
    private final Integer propostaId;

    public RecusarPropostaComando(PropostaService propostaService, Integer propostaId) {
        this.propostaService = propostaService;
        this.propostaId = propostaId;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            propostaService.recusarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Trata exceções do domínio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao recusar proposta: " + e.getMessage());
        }
    }
}