package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Interface de Comando.
 * Define o contrato para todos os comandos de escrita na gestão de Propostas.
 */
public interface ComandoProposta {
    
    /**
     * Executa a ação encapsulada pelo comando, resolvendo a lógica e o ResponseEntity.
     * @return ResponseEntity com o resultado da operação.
     */
    ResponseEntity<?> executar();
}