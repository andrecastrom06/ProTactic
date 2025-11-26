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

    private final EstrategiaRespostaInscricao sucessoEstrategia = new InscricaoSucessoEstrategia();
    private final EstrategiaRespostaInscricao falhaEstrategia = new InscricaoFalhaEstrategia();
    
    public record InscricaoFormulario(
        String atleta,
        String competicao
    ) {}

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarInscricao(@RequestBody InscricaoFormulario formulario) {

        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        try {
            InscricaoAtleta resultado = servicoDominio.registrarInscricaoPorNome(
                formulario.atleta(),
                formulario.competicao()
            );
            
            if (sucessoEstrategia.isAplicavel(resultado)) {
                return sucessoEstrategia.processar(resultado);
            } else if (falhaEstrategia.isAplicavel(resultado)) {
                return falhaEstrategia.processar(resultado); 
            }
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 

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