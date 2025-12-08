package dev.com.protactic.apresentacao.principal.feature_07_definir_capitao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity; // Importante para retornos HTTP corretos
import org.springframework.http.HttpStatus;

import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;
import dev.com.protactic.aplicacao.principal.jogador.JogadorServicoAplicacao;
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.entidade.Capitao;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.servico.CapitaoService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/capitao") 
public class DefinirCapitaoControlador { 

    @Autowired private JogadorServicoAplicacao jogadorServicoAplicacao;
    @Autowired private ClubeServicoAplicacao clubeServicoAplicacao;
    @Autowired private CapitaoService capitaoService; 

    @GetMapping(path = "pesquisa-jogadores")
    public List<JogadorResumo> pesquisarResumosDeJogadores() {
        return jogadorServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-jogadores-por-clube/{clubeId}")
    public List<JogadorResumo> pesquisarResumosDeJogadoresPorClube(@PathVariable("clubeId") Integer clubeId) {
        return jogadorServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }
    
    @GetMapping(path = "buscar-por-clube/{clubeId}")
    public Capitao buscarCapitaoDoClube(@PathVariable("clubeId") Integer clubeId) {
        return capitaoService.buscarCapitaoPorClube(clubeId); 
    }

    @GetMapping(path = "listar-todos")
    public List<ClubeResumo> listarTodosOsCapitaes() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @PostMapping(path = "definir/{jogadorId}") 
    public ResponseEntity<String> definirCapitao(@PathVariable("jogadorId") Integer jogadorId) {
        try {
            capitaoService.definirCapitaoPorId(jogadorId);
            return ResponseEntity.ok("Capitão definido com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao definir capitão: " + e.getMessage());
        }
    }
}