package dev.com.protactic.apresentacao.principal.feature_08_registro_cartoes;

import org.springframework.http.ResponseEntity;

public interface ComandoRegistroCartao {
    
    ResponseEntity<?> executar();
}