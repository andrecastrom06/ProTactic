package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;  
import org.springframework.http.HttpStatus; // 🎯 NOVO
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;
import dev.com.protactic.aplicacao.principal.lesao.LesaoServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.servico.RegistroLesoesServico;

@RestController
@RequestMapping("backend/lesao") 
@CrossOrigin(origins = "http://localhost:3000")
public class RegistroDeLesaoControlador {

    private @Autowired LesaoServicoAplicacao lesaoServicoAplicacao; 
    
    private @Autowired RegistroLesoesServico registroLesoesServico; 

    @GetMapping(path = "pesquisa")
    public List<LesaoResumo> pesquisarResumos() {
        return lesaoServicoAplicacao.pesquisarResumos();
    }
    
    @GetMapping(path = "pesquisa-por-jogador/{jogadorId}")
    public List<LesaoResumo> pesquisarResumosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return lesaoServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }
    
    @GetMapping(path = "pesquisa-ativa-por-jogador/{jogadorId}")
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return lesaoServicoAplicacao.pesquisarResumoAtivoPorJogador(jogadorId);
    }

    public record RegistrarLesaoFormulario(
        int grau
    ) {}
    
    public record PlanoRecuperacaoFormulario(
        String tempo, 
        String plano
    ) {}

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<?> registrarLesao(
            @PathVariable("jogadorId") Integer jogadorId, 
            @RequestBody RegistrarLesaoFormulario formulario) {
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        try {
            registroLesoesServico.registrarLesaoPorId(jogadorId, formulario.grau());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(path = "/cadastrar-plano/{jogadorId}")
    public ResponseEntity<?> cadastrarPlanoRecuperacao(
            @PathVariable("jogadorId") Integer jogadorId, 
            @RequestBody PlanoRecuperacaoFormulario formulario) {

        if (formulario == null) {
             return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }

        try {
            registroLesoesServico.cadastrarPlanoRecuperacaoPorId(
                jogadorId, 
                formulario.tempo(), 
                formulario.plano()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping(path = "/encerrar/{jogadorId}")
    public ResponseEntity<?> encerrarLesao(@PathVariable("jogadorId") Integer jogadorId) { 
        
        try {
            registroLesoesServico.encerrarRecuperacaoPorId(jogadorId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}