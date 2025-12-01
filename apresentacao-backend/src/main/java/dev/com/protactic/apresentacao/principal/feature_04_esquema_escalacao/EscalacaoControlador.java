package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

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
import org.springframework.http.HttpStatus; // 🎯 NOVO

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.servico.DefinirEsquemaTaticoService;



@RestController
@RequestMapping("backend/escalacao")
@CrossOrigin(origins = "http://localhost:3000")
public class EscalacaoControlador {

    private @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;
    
    // Mantido para uso direto
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService; 
    
    // Métodos GET (leitura) permanecem inalterados
    @GetMapping(path = "pesquisa-por-data/{jogoData}/{clubeId}")
    public List<EscalacaoResumo> pesquisarResumosPorData(@PathVariable("jogoData") String jogoData, @PathVariable("clubeId") Integer clubeId) {
        return escalacaoServicoAplicacao.pesquisarResumosPorData(jogoData, clubeId );
    }
    
    @GetMapping(path = "obter-por-data/{jogoData}/{clubeId}")
    public List<String> obterEscalacao(@PathVariable("jogoData") String jogoData, @PathVariable("clubeId") Integer clubeId ) {
        return definirEsquemaTaticoService.obterEscalacao(jogoData, clubeId); 
    }


    public record EscalacaoFormulario(
        String jogoData,
        String nomeJogador,
        Integer clubeId
    ) {}

    /**
     * Lógica do Command movida para o Controlador.
     */
    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrarEscalacao(@RequestBody EscalacaoFormulario formulario) {
        
        // 🎯 LÓGICA DO RegistrarEscalacaoComando MOVIDA PARA CÁ (Incluindo validação e try-catch)
        if (formulario == null || formulario.jogoData() == null || formulario.nomeJogador() == null) {
            return ResponseEntity.badRequest().body("O formulário (jogoData, nomeJogador) não pode ser nulo.");
        }
        
        try {
            boolean sucesso = definirEsquemaTaticoService.registrarJogadorEmEscalacao(
                formulario.jogoData(),
                formulario.nomeJogador(),
                formulario.clubeId()
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