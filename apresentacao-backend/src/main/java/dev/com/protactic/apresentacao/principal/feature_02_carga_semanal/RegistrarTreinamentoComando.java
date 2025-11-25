package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Padrão Command: Comando Concreto para registrar treinamento.
 * Contém a lógica de negócio e o tratamento de HTTP, limpando o Controlador.
 */
public class RegistrarTreinamentoComando implements ComandoCarga {

    private final PlanejamentoCargaSemanalService planejamentoCargaSemanalService;
    private final Integer jogadorId;

    public RegistrarTreinamentoComando(
            PlanejamentoCargaSemanalService planejamentoCargaSemanalService,
            Integer jogadorId) {
        this.planejamentoCargaSemanalService = planejamentoCargaSemanalService;
        this.jogadorId = jogadorId;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            boolean sucesso = planejamentoCargaSemanalService.registrarTreinoPorId(jogadorId);

            if (sucesso) {
                return ResponseEntity.ok().build(); 
            } else {
                return ResponseEntity.badRequest().body("Jogador inapto para o treino (lesionado ou sem contrato).");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}