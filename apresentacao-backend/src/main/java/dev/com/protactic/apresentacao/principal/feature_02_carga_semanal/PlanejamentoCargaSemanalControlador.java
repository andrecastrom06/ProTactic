package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;


@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanejamentoCargaSemanalControlador {

    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService; 

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<?> registrarTreinamento(@PathVariable("jogadorId") Integer jogadorId) {
        
        ComandoCarga comando = new RegistrarTreinamentoComando(
            planejamentoCargaSemanalService, 
            jogadorId
        );
        
        return comando.executar();
    }
}