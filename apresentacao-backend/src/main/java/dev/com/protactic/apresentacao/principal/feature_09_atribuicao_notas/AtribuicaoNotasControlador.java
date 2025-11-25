package dev.com.protactic.apresentacao.principal.feature_09_atribuicao_notas;

import java.util.List;
import java.math.BigDecimal;
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

    // Injeção de dependências de Serviço (Domínio e Aplicação)
    private @Autowired NotaServicoAplicacao notaServicoAplicacao;
    private @Autowired NotaService notaService;

    // 💡 Implementação do Padrão Singleton na Camada de Apresentação
    // Acessa a instância única do Conversor de Notas
    private final ConversorNotaSingleton conversorNotas = ConversorNotaSingleton.getInstance();


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

    /**
     * Atribui uma nota a um jogador em um jogo após validação e formatação.
     * Retorna ResponseEntity<Void> para melhor controle de status HTTP.
     */
    @PostMapping(path = "/atribuir")
    public ResponseEntity<Void> atribuirNotaEObservacao(@RequestBody NotaFormulario formulario) {

        if (formulario == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // 🎯 Uso do Singleton: Validação e formatação da nota de entrada
            // Se a nota for inválida (ex: < 0 ou > 10), o Singleton lança uma exceção.
            BigDecimal notaFormatada = conversorNotas.validarEFormatar(formulario.nota());

            notaService.atribuirNotaEObservacao(
                formulario.jogoId(),
                formulario.jogadorId(),
                notaFormatada, // Usa a nota validada e formatada
                formulario.observacao()
            );

            return ResponseEntity.ok().build(); // 200 OK (ou 201 Created)

        } catch (IllegalArgumentException e) {
            // Captura erros de validação da Apresentação (ex: nota fora do intervalo)
            // Retorna um status de erro adequado (400 Bad Request)
            System.err.println("Erro de validação de dados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}