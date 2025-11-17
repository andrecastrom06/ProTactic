package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;

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
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        try {
            FormacaoServicoAplicacao.FormacaoDados dados = new FormacaoServicoAplicacao.FormacaoDados(
                formulario.partidaId(),
                formulario.esquema(),
                formulario.jogadoresIds()
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

        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }

        try {
            FormacaoServicoAplicacao.FormacaoDados dados = new FormacaoServicoAplicacao.FormacaoDados(
                formulario.partidaId(),
                formulario.esquema(),
                formulario.jogadoresIds()
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
}