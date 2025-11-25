package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EncerrarLesaoComando implements ComandoRegistroLesao {

    private final RegistroLesoesServico registroLesoesServico;
    private final Integer jogadorId;

    public EncerrarLesaoComando(
            RegistroLesoesServico registroLesoesServico,
            Integer jogadorId) {
        this.registroLesoesServico = registroLesoesServico;
        this.jogadorId = jogadorId;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            registroLesoesServico.encerrarRecuperacaoPorId(jogadorId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}