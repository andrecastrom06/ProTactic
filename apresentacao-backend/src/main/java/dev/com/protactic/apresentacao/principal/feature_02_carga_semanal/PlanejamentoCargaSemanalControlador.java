package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculadoraCargaStrategy;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaExponencial;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaLinear;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoCargaSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanejamentoCargaSemanalControlador {

    @Autowired
    private PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    public static class DadosTreinoInput {
        public double duracao;
        public double intensidade;
        public String tipoCalculo; 
    }

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<?> registrarTreinamento(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody DadosTreinoInput dados 
    ) {
        try {
            CalculadoraCargaStrategy estrategia;
            
            if ("EXPONENCIAL".equalsIgnoreCase(dados.tipoCalculo)) {
                estrategia = new CalculoCargaExponencial();
            } else {
                estrategia = new CalculoCargaLinear(); // Padrão
            }

            boolean sucesso = planejamentoCargaSemanalService.registrarTreinoPorId(
                    jogadorId,
                    dados.duracao,    
                    dados.intensidade,
                    estrategia   
            );

            
            double cargaCalculada = estrategia.calcularCarga(dados.duracao, dados.intensidade).doubleValue();

            if (sucesso) {
                return ResponseEntity.ok(Map.of(
                    "mensagem", "Treino registrado com sucesso!",
                    "cargaCalculada", cargaCalculada,
                    "estrategiaUsada", estrategia.getClass().getSimpleName()
                ));
            } else {
                return ResponseEntity.badRequest().body("Jogador inapto para o treino (lesionado ou sem contrato).");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}