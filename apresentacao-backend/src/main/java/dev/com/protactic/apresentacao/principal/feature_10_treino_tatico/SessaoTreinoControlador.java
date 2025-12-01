package dev.com.protactic.apresentacao.principal.feature_10_treino_tatico;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 
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
        
        // 🎯 LÓGICA DO CriarSessaoComando MOVIDA PARA CÁ (Validação, Chamada e Tratamento)
        
        // 1. Validação
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        if (formulario.clubeId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: ID do Clube é obrigatório.");
        }
        if (formulario.partidaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Partida não selecionada.");
        }
        if (formulario.nome() == null || formulario.nome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome da sessão é obrigatório.");
        }
        
        // 2. Execução
        try {
            sessaoTreinoService.criarSessaoPorIds(
                formulario.nome(),
                formulario.partidaId(),
                formulario.convocadosIds(),
                formulario.clubeId() 
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created

        } catch (Exception e) {
            // 3. Tratamento de exceção
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}