package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaServicoAplicacao;
import dev.com.protactic.dominio.principal.InscricaoAtleta;
import dev.com.protactic.dominio.principal.Jogador;


import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoService;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("backend/inscricao")
@CrossOrigin(origins = "http://localhost:3000")

public class InscricaoAtletaControlador {

    private @Autowired InscricaoAtletaServicoAplicacao servicoAplicacao;
    
    private @Autowired RegistroInscricaoService servicoDominio;

    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired RegistroLesoesRepository lesoesRepository;

    public record InscricaoFormulario(
        String atleta,
        String competicao
    ) {}

    @PostMapping("/salvar")
    public InscricaoAtleta salvarInscricao(@RequestBody InscricaoFormulario formulario) {

        Jogador jogador = jogadorRepository.findByNomeIgnoreCase(formulario.atleta())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Jogador " + formulario.atleta() + " não encontrado"));
        
        int idade = jogador.getIdade(); 
        
        boolean contratoAtivo = lesoesRepository.contratoAtivo(formulario.atleta()); 

        return servicoDominio.registrarInscricao(
            formulario.atleta(),
            idade,
            contratoAtivo,
            formulario.competicao()
        );
    }


    @GetMapping("/todos")
    public List<InscricaoAtletaResumo> pesquisarTodos() {
        return servicoAplicacao.pesquisarResumos();
    }

    @GetMapping("/atleta/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorAtleta(@PathVariable("nome") String atleta) {
        return servicoAplicacao.pesquisarResumosPorAtleta(atleta);
    }

    @GetMapping("/competicao/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorCompeticao(@PathVariable("nome") String competicao) {
        return servicoAplicacao.pesquisarResumosPorCompeticao(competicao);
    }
}