package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import java.util.List;
import java.util.Date; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import dev.com.protactic.aplicacao.principal.proposta.PropostaServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico.PropostaService;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico.PropostaService.DadosNovaProposta;

import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpStatus; // 🎯 NOVO IMPORT
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


    public record PropostaFormulario(
        Integer jogadorId,
        Integer clubeId,
        double valor 
    ) {}
    
    public record PropostaValorFormulario(
        double novoValor
    ) {}

    @PostMapping(path = "/criar")
    public ResponseEntity<?> criarProposta(@RequestBody PropostaFormulario formulario) {
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("O formulário não pode ser nulo.");
        }
        
        try {
            DadosNovaProposta dados = new DadosNovaProposta(
                formulario.jogadorId(),
                formulario.clubeId(),
                formulario.valor(),
                new Date()
            );
            
            propostaService.criarPropostaPorIds(dados);
            
            return ResponseEntity.status(HttpStatus.CREATED).build(); 

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar proposta: " + e.getMessage());
        }
    }
    
    
    @PatchMapping(path = "/editar-valor/{propostaId}")
    public ResponseEntity<?> editarValorProposta(
            @PathVariable("propostaId") Integer propostaId,
            @RequestBody PropostaValorFormulario formulario) {
        
        try {
            propostaService.editarValorProposta(propostaId, formulario.novoValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar proposta: " + e.getMessage());
        }
    }

    @PostMapping(path = "/aceitar/{propostaId}")
    public ResponseEntity<?> aceitarProposta(@PathVariable("propostaId") Integer propostaId) {
        
        try {
            propostaService.aceitarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao aceitar proposta: " + e.getMessage());
        }
    }

    
    @PostMapping(path = "/recusar/{propostaId}")
    public ResponseEntity<?> recusarProposta(@PathVariable("propostaId") Integer propostaId) {
        
        try {
            propostaService.recusarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao recusar proposta: " + e.getMessage());
        }
    }
 
    @DeleteMapping(path = "/excluir/{propostaId}")
    public ResponseEntity<?> excluirProposta(@PathVariable("propostaId") Integer propostaId) {
        
        try {
            propostaService.excluirProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir proposta: " + e.getMessage());
        }
    }
}