package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;

@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")

public class PlanejamentoCargaSemanalControlador {

    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    private @Autowired JogadorRepository jogadorRepository;

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<Void> registrarTreinamento(@PathVariable("jogadorId") Integer jogadorId) {
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        boolean sucesso = planejamentoCargaSemanalService.registrarTreino(jogador);

        if (sucesso) {
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}