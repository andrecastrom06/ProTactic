package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AceitarPropostaComando implements ComandoProposta {

    private final PropostaService propostaService;
    private final Integer propostaId;

    public AceitarPropostaComando(PropostaService propostaService, Integer propostaId) {
        this.propostaService = propostaService;
        this.propostaId = propostaId;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            propostaService.aceitarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Trata exceções do domínio
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao aceitar proposta: " + e.getMessage());
        }
    }
}