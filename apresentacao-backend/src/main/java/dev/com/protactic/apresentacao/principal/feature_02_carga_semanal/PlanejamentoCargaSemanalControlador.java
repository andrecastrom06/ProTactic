package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaLinear;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoCargaSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanejamentoCargaSemanalControlador {

    @Autowired
    private PlanejamentoCargaSemanalService planejamentoCargaSemanalService;


    public static class DadosTreinoInput {
        public double duracao;
        public double intensidade;
    }

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<?> registrarTreinamento(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody DadosTreinoInput dados 
    ) {
        
        try {
            CalculoCargaLinear estrategiaPadrao = new CalculoCargaLinear();

            boolean sucesso = planejamentoCargaSemanalService.registrarTreinoPorId(
                    jogadorId,
                    dados.duracao,    
                    dados.intensidade,
                    estrategiaPadrao   
            );

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