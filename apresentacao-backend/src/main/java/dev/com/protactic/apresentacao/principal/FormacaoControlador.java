package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;

@RestController
@RequestMapping("backend/formacao")
@CrossOrigin(origins = "http://localhost:3000")
public class FormacaoControlador {
    
    @Autowired
    private FormacaoServicoAplicacao formacaoServicoAplicacao;

    public record FormacaoFormulario(
        Integer partidaId,
        String esquema,
        List<Integer> jogadoresIds
    ) {}

    @PostMapping(path = "/salvar")
    public ResponseEntity<?> salvarFormacao(@RequestBody FormacaoFormulario formulario) {
        if (formulario == null) return ResponseEntity.badRequest().body("Formulário nulo.");
        try {
            FormacaoServicoAplicacao.FormacaoDados dados = new FormacaoServicoAplicacao.FormacaoDados(
                formulario.partidaId(), formulario.esquema(), formulario.jogadoresIds()
            );
            FormacaoResumo escalacaoSalva = formacaoServicoAplicacao.salvarFormacao(dados);
            return ResponseEntity.ok(escalacaoSalva); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/editar/{formacaoId}")
    public ResponseEntity<?> editarFormacao(
            @PathVariable("formacaoId") Integer formacaoId,
            @RequestBody FormacaoFormulario formulario) {
        if (formulario == null) return ResponseEntity.badRequest().body("Formulário nulo.");
        try {
            FormacaoServicoAplicacao.FormacaoDados dados = new FormacaoServicoAplicacao.FormacaoDados(
                formulario.partidaId(), formulario.esquema(), formulario.jogadoresIds()
            );
            FormacaoResumo escalacaoAtualizada = formacaoServicoAplicacao.editarFormacao(formacaoId, dados);
            return ResponseEntity.ok(escalacaoAtualizada); 
        } catch (Exception e) {
            if (e.getMessage().contains("não encontrada")) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar-por-partida/{partidaId}")
    public ResponseEntity<FormacaoResumo> buscarPorPartida(@PathVariable Integer partidaId) {
        Optional<FormacaoResumo> formacao = formacaoServicoAplicacao.buscarPorPartida(partidaId);
        
        return formacao.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.noContent().build());
    }
}