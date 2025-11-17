package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoServicoAplicacao;

import dev.com.protactic.dominio.principal.SessaoTreino; 
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;


@RestController
@RequestMapping("backend/sessao-treino")
@CrossOrigin(origins = "http://localhost:3000")
public class SessaoTreinoControlador {

    private @Autowired SessaoTreinoServicoAplicacao sessaoTreinoServicoAplicacao;
    private @Autowired SessaoTreinoService sessaoTreinoService;
    

    @GetMapping(path = "pesquisa")
    public List<SessaoTreinoResumo> pesquisarResumos() {
        return sessaoTreinoServicoAplicacao.pesquisarResumos();
    }
    @GetMapping(path = "pesquisa-por-partida/{partidaId}")
    public List<SessaoTreinoResumo> pesquisarResumosPorPartida(@PathVariable("partidaId") Integer partidaId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorPartida(partidaId);
    }
    @GetMapping(path = "pesquisa-por-convocado/{jogadorId}")
    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(@PathVariable("jogadorId") Integer jogadorId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorConvocado(jogadorId);
    }
    @GetMapping(path = "listar-por-nome-partida/{partidaNome}")
    public List<SessaoTreino> listarSessoesPorPartida(@PathVariable("partidaNome") String partidaNome) {
        return sessaoTreinoService.listarSessoesPorPartida(partidaNome);
    }

    public record SessaoTreinoFormulario(
        String nome,
        Integer partidaId,
        List<Integer> convocadosIds
    ) {}

    @PostMapping(path = "/criar")
    public ResponseEntity<?> criarSessao(@RequestBody SessaoTreinoFormulario formulario) {
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        try {
            sessaoTreinoService.criarSessaoPorIds(
                formulario.nome(),
                formulario.partidaId(),
                formulario.convocadosIds()
            );
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}