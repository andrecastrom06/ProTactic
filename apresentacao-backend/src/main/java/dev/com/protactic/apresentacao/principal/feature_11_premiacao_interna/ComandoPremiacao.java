package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import org.springframework.http.ResponseEntity;

public interface ComandoPremiacao {
    
    ResponseEntity<?> executar();
}