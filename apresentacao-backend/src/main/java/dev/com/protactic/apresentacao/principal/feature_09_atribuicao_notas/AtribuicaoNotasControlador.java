package dev.com.protactic.apresentacao.principal.feature_09_atribuicao_notas;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode; // 🎯 IMPORT NECESSÁRIO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private BigDecimal validarEFormatarNota(BigDecimal notaAConverter) {
        if (notaAConverter == null) {
            throw new IllegalArgumentException("A nota não pode ser nula.");
        }

        if (notaAConverter.compareTo(BigDecimal.ZERO) < 0 || notaAConverter.compareTo(new BigDecimal("10.00")) > 0) {
            throw new IllegalArgumentException("A nota deve estar no intervalo de 0.00 a 10.00.");
        }

        return notaAConverter.setScale(2, RoundingMode.HALF_UP);
    }

    @PostMapping(path = "/atribuir")
    public ResponseEntity<Void> atribuirNotaEObservacao(@RequestBody NotaFormulario formulario) {

        if (formulario == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            BigDecimal notaFormatada = validarEFormatarNota(formulario.nota());

            notaService.atribuirNotaEObservacao(
                formulario.jogoId(),
                formulario.jogadorId(),
                notaFormatada, 
                formulario.observacao()
            );

            return ResponseEntity.ok().build(); 

        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação de dados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}