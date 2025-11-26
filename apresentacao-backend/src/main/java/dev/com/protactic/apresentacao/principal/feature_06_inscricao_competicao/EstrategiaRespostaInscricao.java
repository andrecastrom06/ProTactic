package dev.com.protactic.apresentacao.principal.feature_06_inscricao_competicao;

import dev.com.protactic.dominio.principal.InscricaoAtleta;
import org.springframework.http.ResponseEntity;

public interface EstrategiaRespostaInscricao {
    
    boolean isAplicavel(InscricaoAtleta resultado);

    ResponseEntity<?> processar(InscricaoAtleta resultado);
}