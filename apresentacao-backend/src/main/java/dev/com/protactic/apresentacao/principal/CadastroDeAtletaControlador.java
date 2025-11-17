package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Date; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpStatus; 

import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/clube")
public class CadastroDeAtletaControlador {

    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    private @Autowired CadastroDeAtletaService cadastroDeAtletaService;
    @GetMapping(path = "pesquisa")
    public List<ClubeResumo> pesquisarResumos() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-competicao/{id}")
    public List<ClubeResumo> pesquisarResumosPorCompeticao(@PathVariable("id") Integer competicaoId) {
        return clubeServicoAplicacao.pesquisarResumosPorCompeticao(competicaoId);
    }

    
    public record ContratacaoFormulario(
        Integer jogadorId,
        Date data
    ) {}

    @PostMapping(path = "{clubeId}/contratar")
    public ResponseEntity<?> contratarAtleta( 
            @PathVariable("clubeId") Integer clubeId, 
            @RequestBody ContratacaoFormulario formulario) { 
        
        if (formulario == null || formulario.jogadorId() == null || formulario.data() == null) {
            return ResponseEntity
                .badRequest()
                .body("O formulário de contratação (com jogadorId e data) não pode ser nulo.");
        }

        try {
            boolean resultado = cadastroDeAtletaService.contratarPorId(
                clubeId, 
                formulario.jogadorId(), 
                formulario.data()
            );

            if (resultado) {
                return ResponseEntity.ok().build(); 
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contratação não permitida (Ex: Janela fechada)."); 
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
  
}