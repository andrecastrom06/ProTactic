package dev.com.protactic.apresentacao.principal;

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

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
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
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired ClubeRepository clubeRepository;
    


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

    @PostMapping(path = "/criar")
    public void criarProposta(@RequestBody PropostaFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }


        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + formulario.jogadorId());
        }

        Clube clubePropositor = clubeRepository.buscarPorId(formulario.clubeId());
        if (clubePropositor == null) {
            throw new RuntimeException("Clube propositor não encontrado: " + formulario.clubeId());
        }

        try {
            propostaService.criarProposta(
                jogador, 
                clubePropositor, 
                formulario.valor(), 
                new Date()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar criar a proposta: " + e.getMessage(), e);
        }
    }
    
    public record PropostaValorFormulario(
        double novoValor
    ) {}

    @PatchMapping(path = "/editar-valor/{propostaId}")
    public ResponseEntity<Void> editarValorProposta(
            @PathVariable("propostaId") Integer propostaId,
            @RequestBody PropostaValorFormulario formulario) {
        try {
            propostaService.editarValorProposta(propostaId, formulario.novoValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar proposta: " + e.getMessage(), e);
        }
    }

    @PostMapping(path = "/aceitar/{propostaId}")
    public ResponseEntity<Void> aceitarProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.aceitarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao aceitar proposta: " + e.getMessage(), e);
        }
    }


    @PostMapping(path = "/recusar/{propostaId}")
    public ResponseEntity<Void> recusarProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.recusarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recusar proposta: " + e.getMessage(), e);
        }
    }

 
    @DeleteMapping(path = "/excluir/{propostaId}")
    public ResponseEntity<Void> excluirProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.excluirProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir proposta: " + e.getMessage(), e);
        }
    }
}