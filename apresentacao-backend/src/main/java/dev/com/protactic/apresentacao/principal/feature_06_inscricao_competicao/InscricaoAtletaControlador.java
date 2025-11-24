package dev.com.protactic.apresentacao.principal.feature_06_inscricao_competicao;

import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaServicoAplicacao;
import dev.com.protactic.dominio.principal.InscricaoAtleta;

import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("backend/inscricao")
@CrossOrigin(origins = "http://localhost:3000")
public class InscricaoAtletaControlador {

    private @Autowired InscricaoAtletaServicoAplicacao servicoAplicacao;
    
    private @Autowired RegistroInscricaoService servicoDominio;

    public record InscricaoFormulario(
        String atleta,
        String competicao
    ) {}

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarInscricao(@RequestBody InscricaoFormulario formulario) {

        try {
            InscricaoAtleta resultado = servicoDominio.registrarInscricaoPorNome(
                formulario.atleta(),
                formulario.competicao()
            );
            
            if (!resultado.isInscrito()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
            
            
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/todos")
    public List<InscricaoAtletaResumo> pesquisarTodos() {
        return servicoAplicacao.pesquisarResumos();
    }

    @GetMapping("/atleta/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorAtleta(@PathVariable("nome") String atleta) {
        return servicoAplicacao.pesquisarResumosPorAtleta(atleta);
    }

    @GetMapping("/competicao/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorCompeticao(@PathVariable("nome") String competicao) {
        return servicoAplicacao.pesquisarResumosPorCompeticao(competicao);
    }
}