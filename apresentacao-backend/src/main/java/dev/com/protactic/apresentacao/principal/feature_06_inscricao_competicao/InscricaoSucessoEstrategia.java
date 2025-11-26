package dev.com.protactic.apresentacao.principal.feature_06_inscricao_competicao;

import dev.com.protactic.dominio.principal.InscricaoAtleta;
import org.springframework.http.ResponseEntity;

public class InscricaoSucessoEstrategia implements EstrategiaRespostaInscricao {
    
    @Override
    public boolean isAplicavel(InscricaoAtleta resultado) {
        return resultado.isInscrito();
    }

    @Override
    public ResponseEntity<?> processar(InscricaoAtleta resultado) {
        return ResponseEntity.ok(resultado); 
    }
}