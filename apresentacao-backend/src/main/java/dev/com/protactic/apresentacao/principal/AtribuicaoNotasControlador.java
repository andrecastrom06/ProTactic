package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.math.BigDecimal; // Importante para o DTO e o Serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // Importante para o POST
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.nota.NotaResumo;
import dev.com.protactic.aplicacao.principal.nota.NotaServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos)
import dev.com.protactic.dominio.principal.nota.NotaService;


@RestController
@RequestMapping("backend/nota")
@CrossOrigin(origins = "http://localhost:3000")
public class AtribuicaoNotasControlador {

    // --- Injeção dos Serviços ---

    // 1. Serviço de APLICAÇÃO (para Consultas/Queries)
    private @Autowired NotaServicoAplicacao notaServicoAplicacao;

    // 2. Serviço de DOMÍNIO (para Comandos/Lógica de Negócio)
    private @Autowired NotaService notaService;

    
    // --- Endpoints de CONSULTA (GET) ---
    // (Usam o NotaServicoAplicacao)

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
        
        // 1. Validamos se o formulário não é nulo
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }

        // 2. Chama o Serviço de DOMÍNIO para executar o comando
        // (O seu CML pedia exatamente estes 4 argumentos)
        notaService.atribuirNotaEObservacao(
            formulario.jogoId(),
            formulario.jogadorId(),
            formulario.nota(),
            formulario.observacao()
        );
    }
} 