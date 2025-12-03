package dev.com.protactic.apresentacao.principal.feature_09_atribuicao_notas;

import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import dev.com.protactic.aplicacao.principal.partida.PartidaServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.servico.PartidaService;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.servico.PartidaService.DadosDesempenhoAtleta;

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

    @PostMapping(path = "/registrar-estatisticas")
    public ResponseEntity<Void> registrarEstatisticas(@RequestBody List<DadosDesempenhoAtleta> formulario) {
        try {
            if (formulario == null || formulario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            partidaService.registrarEstatisticas(formulario);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public record AtualizarPlacarFormulario(
        Integer placarCasa, 
        Integer placarVisitante
    ) {}

    @PutMapping(path = "/{id}/placar")
    public ResponseEntity<Void> atualizarPlacar(
            @PathVariable Integer id, 
            @RequestBody AtualizarPlacarFormulario form) {
        try {
            partidaService.atualizarPlacar(
                id, 
                form.placarCasa(), 
                form.placarVisitante()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}