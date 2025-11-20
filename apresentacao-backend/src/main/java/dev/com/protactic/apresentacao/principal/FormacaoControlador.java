package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;

// IMPORTS QUE FALTAVAM
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; // <--- Corrigi o erro "cannot find symbol class Optional"

@RestController
@RequestMapping("/backend/formacao")
@CrossOrigin(origins = "http://localhost:3000")
public class FormacaoControlador {

    private final FormacaoServicoAplicacao formacaoServicoAplicacao;

    public FormacaoControlador(FormacaoServicoAplicacao formacaoServicoAplicacao) {
        this.formacaoServicoAplicacao = formacaoServicoAplicacao;
    }


    public record FormacaoFormulario(
        Integer partidaId, 
        String esquema, 
        List<Integer> jogadoresIds,
        Integer clubeId
    ) {}

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarFormacao(@RequestBody FormacaoFormulario form) {
        try {
            if (form.clubeId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de segurança: ID do clube não informado.");
            }

            var dadosServico = new FormacaoServicoAplicacao.FormacaoDados(
                form.partidaId(),
                form.esquema(),
                form.jogadoresIds(),
                form.clubeId() 
            );

            FormacaoResumo resumo = formacaoServicoAplicacao.salvarFormacao(dadosServico);
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-por-partida/{partidaId}")
    public ResponseEntity<?> buscarPorPartida(@PathVariable Integer partidaId, @RequestParam(required = false) Integer clubeId) {
        
        Optional<FormacaoResumo> resumo = formacaoServicoAplicacao.buscarPorPartida(partidaId);
        
        if (resumo.isPresent()) {
             return ResponseEntity.ok(resumo.get());
        } else {
             return ResponseEntity.noContent().build();
        }
    }
}