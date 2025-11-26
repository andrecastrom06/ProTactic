package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;

import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;


@RestController
@RequestMapping("backend/premiacao")
@CrossOrigin(origins = "http://localhost:3000")
public class PremiacaoControlador {

    private @Autowired PremiacaoServicoAplicacao servicoAplicacao;
    
    // Mantido como Receiver (Será injetado no Comando)
    private @Autowired PremiacaoService servicoDominio;

    public record PremiacaoFormulario(
        Integer jogadorId,
        String nome,
        @JsonFormat(pattern = "yyyy-MM-dd") Date dataPremiacao
    ) {}

    /**
     * Padrão Command (Invoker): Cria e executa o comando de salvar premiação.
     */
    @PostMapping("/salvar")
    public ResponseEntity<?> salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        
        // 🎯 Cria o objeto de Comando que encapsula a lógica e o DTO
        ComandoPremiacao comando = new SalvarPremiacaoComando(servicoDominio, formulario);
        
        // Executa o Comando, que resolve toda a validação, criação de DTO e status HTTP.
        return comando.executar();
    }


    // --- Métodos de Leitura (GET) Inalterados ---
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