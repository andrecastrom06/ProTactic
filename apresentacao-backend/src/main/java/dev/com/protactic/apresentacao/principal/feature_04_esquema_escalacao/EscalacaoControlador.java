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


import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;



@RestController
@RequestMapping("backend/escalacao")
@CrossOrigin(origins = "http://localhost:3000")
public class EscalacaoControlador {

    private @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;
    
    // Mantido para injeção no Comando (Receiver) e uso no GET
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService; 
    
    // Métodos GET (leitura) permanecem inalterados
    @GetMapping(path = "pesquisa-por-data/{jogoData}/{clubeId}")
    public List<EscalacaoResumo> pesquisarResumosPorData(@PathVariable("jogoData") String jogoData, @PathVariable("clubeId") Integer clubeId) {
        return escalacaoServicoAplicacao.pesquisarResumosPorData(jogoData, clubeId );
    }
    
    // O erro no path e o clubeId fixo foi corrigido aqui
    @GetMapping(path = "obter-por-data/{jogoData}/{clubeId}")
    public List<String> obterEscalacao(@PathVariable("jogoData") String jogoData, @PathVariable("clubeId") Integer clubeId ) {
        // Assume que '0' no seu código original era um placeholder e agora usa o clubeId da URL
        return definirEsquemaTaticoService.obterEscalacao(jogoData, clubeId); 
    }


    public record EscalacaoFormulario(
        String jogoData,
        String nomeJogador,
        Integer clubeId
    ) {}

    /**
     * Padrão Command (Invoker): Registra Escalacao.
     */
    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrarEscalacao(@RequestBody EscalacaoFormulario formulario) {
        
        // 🎯 Cria o Command, injetando o Service (Receiver) e o Formulário (Request)
        ComandoEscalacao comando = new RegistrarEscalacaoComando(
            definirEsquemaTaticoService,
            formulario
        );
        
        // Executa o Command, delegando toda a lógica de negócio e resposta HTTP
        return comando.executar();
    }
}