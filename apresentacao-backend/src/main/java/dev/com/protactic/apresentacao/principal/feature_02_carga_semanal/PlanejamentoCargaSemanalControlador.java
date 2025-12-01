package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // 🎯 NOVO
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanejamentoCargaSemanalControlador {

    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService; 

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<?> registrarTreinamento(@PathVariable("jogadorId") Integer jogadorId) {
        
        // 🎯 LÓGICA DO RegistrarTreinamentoComando MOVIDA PARA CÁ
        try {
            boolean sucesso = planejamentoCargaSemanalService.registrarTreinoPorId(jogadorId);

            if (sucesso) {
                return ResponseEntity.ok().build(); 
            } else {
                // A mensagem de erro específica do comando foi movida para cá.
                return ResponseEntity.badRequest().body("Jogador inapto para o treino (lesionado ou sem contrato).");
            }
        } catch (Exception e) {
            // O tratamento de exceção do comando foi movido para cá.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}