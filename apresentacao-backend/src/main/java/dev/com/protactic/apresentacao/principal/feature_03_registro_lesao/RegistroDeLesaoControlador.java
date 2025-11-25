package dev.com.protactic.apresentacao.principal.feature_03_registro_lesao;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;
import dev.com.protactic.aplicacao.principal.lesao.LesaoServicoAplicacao;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;

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
        
        ComandoRegistroLesao comando = new RegistrarLesaoComando(
            registroLesoesServico, 
            jogadorId, 
            formulario
        );
        
        return comando.executar();
    }

    @PostMapping(path = "/cadastrar-plano/{jogadorId}")
    public ResponseEntity<?> cadastrarPlanoRecuperacao(
            @PathVariable("jogadorId") Integer jogadorId, 
            @RequestBody PlanoRecuperacaoFormulario formulario) {

        ComandoRegistroLesao comando = new CadastrarPlanoComando(
            registroLesoesServico, 
            jogadorId, 
            formulario
        );
        
        return comando.executar();
    }
    
    @PostMapping(path = "/encerrar/{jogadorId}")
    public ResponseEntity<?> encerrarLesao(@PathVariable("jogadorId") Integer jogadorId) { 
        
        ComandoRegistroLesao comando = new EncerrarLesaoComando(
            registroLesoesServico, 
            jogadorId
        );
        
        return comando.executar();
    }
}