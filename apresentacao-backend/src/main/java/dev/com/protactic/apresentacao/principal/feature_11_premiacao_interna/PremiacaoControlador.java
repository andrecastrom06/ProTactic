package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 

import dev.com.protactic.aplicacao.principal.nota.NotaServicoAplicacao;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;
import dev.com.protactic.dominio.principal.Premiacao; 
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService.DadosPremiacao; // 🎯 NOVO IMPORT


@RestController
@RequestMapping("backend/premiacao")
@CrossOrigin(origins = "http://localhost:3000")
public class PremiacaoControlador {

    private @Autowired PremiacaoServicoAplicacao servicoAplicacao;
    private @Autowired PremiacaoService servicoDominio;
    private @Autowired NotaServicoAplicacao notaServicoAplicacao;

    public record PremiacaoFormulario(
        String nome, 
        @JsonFormat(pattern = "yyyy-MM-dd") Date dataPremiacao 
    ) {}
    
    private void validarFormulario(PremiacaoFormulario formulario) {
        if (formulario == null) {
             throw new IllegalArgumentException("O formulário não pode ser nulo.");
        }
        if (formulario.nome() == null || formulario.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da premiação é obrigatório.");
        }
        if (formulario.dataPremiacao() == null) {
            throw new IllegalArgumentException("A data da premiação é obrigatória.");
        }
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        try {
            validarFormulario(formulario);
            
            Integer jogadorIdGanhador = notaServicoAplicacao.encontrarJogadorComMelhorNotaNoMes(
                formulario.dataPremiacao()
            );

            if (jogadorIdGanhador == null) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar um jogador com nota para o período. A premiação não pode ser criada.");
            }
            
            DadosPremiacao dados = new DadosPremiacao(
                jogadorIdGanhador,
                formulario.nome(),
                formulario.dataPremiacao()
            );

            Premiacao premiacaoSalva = servicoDominio.salvarPremiacaoPorDados(dados);
            
            return ResponseEntity.ok(premiacaoSalva); 

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a premiação: " + e.getMessage());
        }
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