package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.math.BigDecimal; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.nota.NotaResumo;
import dev.com.protactic.aplicacao.principal.nota.NotaServicoAplicacao;

import dev.com.protactic.dominio.principal.nota.NotaService;


@RestController
@RequestMapping("backend/nota")
@CrossOrigin(origins = "http://localhost:3000")
public class AtribuicaoNotasControlador {

  
    private @Autowired NotaServicoAplicacao notaServicoAplicacao;

   
    private @Autowired NotaService notaService;

    
   
    @GetMapping(path = "pesquisa-por-jogo/{jogoId}")
    public List<NotaResumo> pesquisarResumosPorJogo(@PathVariable("jogoId") String jogoId) {
        return notaServicoAplicacao.pesquisarResumosPorJogo(jogoId);
    }

    @GetMapping(path = "pesquisa-por-jogador/{jogadorId}")
    public List<NotaResumo> pesquisarResumosPorJogador(@PathVariable("jogadorId") String jogadorId) {
        return notaServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }


    public record NotaFormulario(
        String jogoId,
        String jogadorId,
        BigDecimal nota,
        String observacao
    ) {}

    @PostMapping(path = "/atribuir")
    public void atribuirNotaEObservacao(@RequestBody NotaFormulario formulario) {
        
       
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }

   
        notaService.atribuirNotaEObservacao(
            formulario.jogoId(),
            formulario.jogadorId(),
            formulario.nota(),
            formulario.observacao()
        );
    }
} 