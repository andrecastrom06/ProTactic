package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

import org.springframework.http.ResponseEntity;

public interface ComandoEscalacao {
    ResponseEntity<?> executar();
}