package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoRepository;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("backend/premiacao")
@CrossOrigin(origins = "http://localhost:3000")

public class PremiacaoControlador {

    private @Autowired PremiacaoServicoAplicacao servicoAplicacao;
    
    private @Autowired PremiacaoRepository repositorioDominio;

    private @Autowired JogadorRepository jogadorRepository;

    public record PremiacaoFormulario(
        Integer jogadorId,
        String nome,
        @JsonFormat(pattern = "yyyy-MM-dd") Date dataPremiacao
    ) {}

    @PostMapping("/salvar")
    public void salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        
        if (formulario.jogadorId() == null) {
            throw new IllegalArgumentException("O 'jogadorId' é obrigatório.");
        }
        
        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador com ID " + formulario.jogadorId() + " não encontrado.");
        }
        
        Premiacao novoPremio = new Premiacao(
            0,
            jogador, 
            formulario.nome(),
            formulario.dataPremiacao()
        );

        repositorioDominio.salvarPremiacao(novoPremio);
    }


    @GetMapping("/todos")
    public List<PremiacaoResumo> pesquisarTodos() {
        return servicoAplicacao.pesquisarResumos();
    }

    @GetMapping("/jogador/{id}")
    public List<PremiacaoResumo> pesquisarPorJogador(@PathVariable("id") Integer jogadorId) {
        return servicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }

    @GetMapping("/nome/{nome}")
    public List<PremiacaoResumo> pesquisarPorNome(@PathVariable("nome") String nome) {
        return servicoAplicacao.pesquisarResumosPorNome(nome);
    }
}