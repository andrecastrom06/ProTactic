package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;

import dev.com.protactic.aplicacao.principal.nota.NotaServicoAplicacao;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;


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

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        
        // 🎯 Cria o objeto de Comando, passando o novo serviço para a lógica de seleção.
        ComandoPremiacao comando = new SalvarPremiacaoComando(servicoDominio, formulario, notaServicoAplicacao);
        
        return comando.executar();
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