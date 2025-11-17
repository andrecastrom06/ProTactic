package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Date; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity; 
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/clube")
public class CadastroDeAtletaControlador {

    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    private @Autowired CadastroDeAtletaService cadastroDeAtletaService;
    private @Autowired ClubeRepository clubeRepository;
    private @Autowired JogadorRepository jogadorRepository;
    

    @GetMapping(path = "pesquisa")
    public List<ClubeResumo> pesquisarResumos() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-competicao/{id}")
    public List<ClubeResumo> pesquisarResumosPorCompeticao(@PathVariable("id") Integer competicaoId) {
        return clubeServicoAplicacao.pesquisarResumosPorCompeticao(competicaoId);
    }

    
    public record ContratacaoFormulario(
        Integer jogadorId,
        Date data
    ) {}

    @PostMapping(path = "{clubeId}/contratar")
    public ResponseEntity<Void> contratarAtleta(
            @PathVariable("clubeId") Integer clubeId, 
            @RequestBody ContratacaoFormulario formulario) { 
        
        if (formulario == null || formulario.jogadorId() == null || formulario.data() == null) {
            throw new IllegalArgumentException("O formulário de contratação (com jogadorId e data) não pode ser nulo.");
        }

        Clube clube = clubeRepository.buscarPorId(clubeId);
        if (clube == null) {
            throw new RuntimeException("Clube não encontrado: " + clubeId);
        }

        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + formulario.jogadorId());
        }

        boolean resultado = cadastroDeAtletaService.contratar(clube, jogador, formulario.data());

        if (resultado) {
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.badRequest().build(); 
        }
    }
  
}