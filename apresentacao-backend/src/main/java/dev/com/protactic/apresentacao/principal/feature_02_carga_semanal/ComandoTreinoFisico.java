package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import org.springframework.http.ResponseEntity;

public interface ComandoTreinoFisico {
    
    ResponseEntity<?> executar();
}