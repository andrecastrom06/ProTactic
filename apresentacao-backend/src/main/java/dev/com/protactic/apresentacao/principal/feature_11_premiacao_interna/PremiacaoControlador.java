package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import java.util.Date; // Corrigido de LocalDate para Date
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;


@RestController
@RequestMapping("backend/premiacao")
@CrossOrigin(origins = "http://localhost:3000")
public class PremiacaoControlador {

    private @Autowired PremiacaoServicoAplicacao servicoAplicacao;
    private @Autowired PremiacaoService servicoDominio;

    public record PremiacaoFormulario(
        Integer jogadorId,
        String nome,
        @JsonFormat(pattern = "yyyy-MM-dd") Date dataPremiacao
    ) {}

    @PostMapping("/salvar")
    public ResponseEntity<Premiacao> salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        
        if (formulario.jogadorId() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            PremiacaoService.DadosPremiacao dados = new PremiacaoService.DadosPremiacao(
                formulario.jogadorId(),
                formulario.nome(),
                formulario.dataPremiacao()
            );

            Premiacao premiacaoSalva = servicoDominio.salvarPremiacaoPorDados(dados);
            
            return ResponseEntity.ok(premiacaoSalva);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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