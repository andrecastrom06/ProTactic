package dev.com.protactic.apresentacao.principal;

import java.util.List;
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

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoServicoAplicacao;

import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;

@RestController
@RequestMapping("backend/escalacao")
@CrossOrigin(origins = "http://localhost:3000")
public class EscalacaoControlador {

    private @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService;
    
    @GetMapping(path = "pesquisa-por-data/{jogoData}")
    public List<EscalacaoResumo> pesquisarResumosPorData(@PathVariable("jogoData") String jogoData) {
        return escalacaoServicoAplicacao.pesquisarResumosPorData(jogoData);
    }
    
    @GetMapping(path = "obter-por-data/{jogoData}")
    public List<String> obterEscalacao(@PathVariable("jogoData") String jogoData) {
        return definirEsquemaTaticoService.obterEscalacao(jogoData);
    }


    public record EscalacaoFormulario(
        String jogoData,
        String nomeJogador
    ) {}

    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrarEscalacao(@RequestBody EscalacaoFormulario formulario) {
        
        if (formulario == null || formulario.jogoData() == null || formulario.nomeJogador() == null) {
            return ResponseEntity.badRequest().body("O formulário (jogoData, nomeJogador) não pode ser nulo.");
        }

        try {
            boolean sucesso = definirEsquemaTaticoService.registrarJogadorEmEscalacao(
                formulario.jogoData(),
                formulario.nomeJogador()
            );

            if (sucesso) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Jogador inapto para a partida (lesionado, suspenso ou sem contrato).");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}