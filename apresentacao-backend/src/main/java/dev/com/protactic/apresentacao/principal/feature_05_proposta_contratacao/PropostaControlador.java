package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import dev.com.protactic.aplicacao.principal.proposta.PropostaServicoAplicacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;

import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.CrossOrigin; 


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/proposta")
public class PropostaControlador {

    
    private @Autowired PropostaServicoAplicacao propostaServicoAplicacao;
    
    
    private @Autowired PropostaService propostaService;
    
    
    @GetMapping(path = "pesquisa")
    public List<PropostaResumo> pesquisarResumos() {
        return propostaServicoAplicacao.pesquisarResumos();
    }
    
    @GetMapping(path = "pesquisa-por-jogador/{jogadorId}")
    public List<PropostaResumo> pesquisarResumosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return propostaServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }
    
    @GetMapping(path = "pesquisa-por-propositor/{clubeId}")
    public List<PropostaResumo> pesquisarResumosPorPropositor(@PathVariable("clubeId") Integer clubeId) {
        return propostaServicoAplicacao.pesquisarResumosPorPropositor(clubeId);
    }
    
    @GetMapping(path = "pesquisa-por-receptor/{clubeId}")
    public List<PropostaResumo> pesquisarResumosPorReceptor(@PathVariable("clubeId") Integer clubeId) {
        return propostaServicoAplicacao.pesquisarResumosPorReceptor(clubeId);
    }
    // --- Fim dos Métodos de Leitura ---


    // Formulários (Mantidos no Controlador)
    public record PropostaFormulario(
        Integer jogadorId,
        Integer clubeId,
        double valor 
    ) {}
    
    public record PropostaValorFormulario(
        double novoValor
    ) {}

    /**
     * Padrão Command (Invoker): Cria Proposta.
     */
    @PostMapping(path = "/criar")
    public ResponseEntity<?> criarProposta(@RequestBody PropostaFormulario formulario) {
        
        ComandoProposta comando = new CriarPropostaComando(propostaService, formulario);
        return comando.executar();
    }
    
    
    @PatchMapping(path = "/editar-valor/{propostaId}")
    public ResponseEntity<?> editarValorProposta(
            @PathVariable("propostaId") Integer propostaId,
            @RequestBody PropostaValorFormulario formulario) {
        
        ComandoProposta comando = new EditarValorPropostaComando(propostaService, propostaId, formulario);
        return comando.executar();
    }

    /**
     * Padrão Command (Invoker): Aceita Proposta.
     * Tipo de retorno agora é `ResponseEntity<?>` (Resolve o erro de compilação).
     */
    @PostMapping(path = "/aceitar/{propostaId}")
    public ResponseEntity<?> aceitarProposta(@PathVariable("propostaId") Integer propostaId) {
        
        ComandoProposta comando = new AceitarPropostaComando(propostaService, propostaId);
        return comando.executar();
    }

    
    @PostMapping(path = "/recusar/{propostaId}")
    public ResponseEntity<?> recusarProposta(@PathVariable("propostaId") Integer propostaId) {
        
        ComandoProposta comando = new RecusarPropostaComando(propostaService, propostaId);
        return comando.executar();
    }
 
    /**
     * Padrão Command (Invoker): Exclui Proposta.
     * Tipo de retorno agora é `ResponseEntity<?>` (Resolve o erro de compilação).
     */
    @DeleteMapping(path = "/excluir/{propostaId}")
    public ResponseEntity<?> excluirProposta(@PathVariable("propostaId") Integer propostaId) {
        
        ComandoProposta comando = new ExcluirPropostaComando(propostaService, propostaId);
        return comando.executar();
    }
}