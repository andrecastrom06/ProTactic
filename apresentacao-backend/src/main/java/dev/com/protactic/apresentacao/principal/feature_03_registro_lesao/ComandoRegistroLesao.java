package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import org.springframework.http.ResponseEntity;

public interface ComandoRegistroLesao {
    
    ResponseEntity<?> executar();
}