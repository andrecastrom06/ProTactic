package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import dev.com.protactic.aplicacao.principal.partida.PartidaServicoAplicacao;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.partida.PartidaService;

// 1. IMPORTAÇÃO CRUCIAL
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("backend/partida")
@CrossOrigin(origins = "http://localhost:3000")
public class PartidaControlador {

    @Autowired
    private PartidaServicoAplicacao servicoAplicacao;
    
    @Autowired
    private PartidaService partidaService;

    @GetMapping(path = "pesquisa")
    public List<PartidaResumo> pesquisarResumos() {
        return servicoAplicacao.pesquisarResumos();
    }
    
    public record CriarPartidaFormulario(
        Integer clubeCasaId,
        Integer clubeVisitanteId,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
        Date dataJogo,
        String hora
    ) {}

    @PostMapping(path = "/criar")
    public ResponseEntity<Partida> criarPartida(@RequestBody CriarPartidaFormulario form) {
        try {
            Partida nova = partidaService.criarPartida(
                form.clubeCasaId(),
                form.clubeVisitanteId(),
                form.dataJogo(),
                form.hora()
            );
            return ResponseEntity.ok(nova);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().build();
        }
    }
}