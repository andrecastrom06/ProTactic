package dev.com.protactic.apresentacao.principal.feature_10_treino_tatico;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoServicoAplicacao;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;


@RestController
@RequestMapping("backend/sessao-treino")
@CrossOrigin(origins = "http://localhost:3000")
public class SessaoTreinoControlador {

    @Autowired private SessaoTreinoServicoAplicacao sessaoTreinoServicoAplicacao;
    
    @Autowired private SessaoTreinoService sessaoTreinoService;
    
    @GetMapping(path = "/pesquisa")
    public List<SessaoTreinoResumo> pesquisarResumos() {
        return sessaoTreinoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "/listar-por-partida/{partidaId}")
    public List<SessaoTreinoResumo> listarSessoesPorPartida(@PathVariable("partidaId") Integer partidaId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorPartida(partidaId);
    }

    @GetMapping(path = "/pesquisa-por-convocado/{jogadorId}")
    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(@PathVariable("jogadorId") Integer jogadorId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorConvocado(jogadorId);
    }

    public record SessaoTreinoFormulario(
        String nome,
        Integer partidaId,
        List<Integer> convocadosIds,
        Integer clubeId 
    ) {}

    
    @PostMapping(path = "/criar")
    public ResponseEntity<?> criarSessao(@RequestBody SessaoTreinoFormulario formulario) {
        
        ComandoSessaoTreino comando = new CriarSessaoComando(sessaoTreinoService, formulario);
        
        return comando.executar();
    }
}