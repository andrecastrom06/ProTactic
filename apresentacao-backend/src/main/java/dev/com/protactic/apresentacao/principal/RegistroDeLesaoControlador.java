package dev.com.protactic.apresentacao.principal;

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

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;


@RestController
@RequestMapping("backend/lesao") 
@CrossOrigin(origins = "http://localhost:3000")

public class RegistroDeLesaoControlador {

    private @Autowired LesaoServicoAplicacao lesaoServicoAplicacao;
    private @Autowired RegistroLesoesServico registroLesoesServico;
    private @Autowired LesaoRepository lesaoRepository;
    private @Autowired JogadorRepository jogadorRepository;

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

    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<Void> registrarLesao(
            @PathVariable("jogadorId") Integer jogadorId, 
            @RequestBody RegistrarLesaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            registroLesoesServico.registrarLesao(jogador.getNome(), formulario.grau());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar lesão para " + jogador.getNome() + ": " + e.getMessage(), e);
        }
    }

    
    public record PlanoRecuperacaoFormulario(
        String tempo, 
        String plano
    ) {}

    @PostMapping(path = "/cadastrar-plano/{jogadorId}")
    public ResponseEntity<Void> cadastrarPlanoRecuperacao(
            @PathVariable("jogadorId") Integer jogadorId, 
            @RequestBody PlanoRecuperacaoFormulario formulario) {

        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());
        if (lesaoOpt.isEmpty()) {
            return ResponseEntity.status(404).body(null); 
        }
        
        Lesao lesao = lesaoOpt.get();

        lesao.setTempo(formulario.tempo());
        lesao.setPlano(formulario.plano());
        
        lesaoRepository.salvar(lesao);
        return ResponseEntity.ok().build();
    }
    @PostMapping(path = "/encerrar/{jogadorId}")
    public ResponseEntity<Void> encerrarLesao(@PathVariable("jogadorId") Integer jogadorId) { 
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            registroLesoesServico.encerrarRecuperacao(jogador.getNome());
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar encerrar lesão para ID " + jogadorId + ": " + e.getMessage(), e);
        }
    }
}