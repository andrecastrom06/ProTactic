package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService.DadosPremiacao;
import dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna.PremiacaoControlador.PremiacaoFormulario;
import dev.com.protactic.aplicacao.principal.nota.NotaServicoAplicacao; // NOVO IMPORT

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class SalvarPremiacaoComando implements ComandoPremiacao {

    private final PremiacaoService servicoDominio;
    private final PremiacaoFormulario formulario;
    private final NotaServicoAplicacao notaServicoAplicacao; // NOVO CAMPO

    // CONSTRUTOR MODIFICADO
    public SalvarPremiacaoComando(PremiacaoService servicoDominio, PremiacaoFormulario formulario, NotaServicoAplicacao notaServicoAplicacao) {
        this.servicoDominio = servicoDominio;
        this.formulario = formulario;
        this.notaServicoAplicacao = notaServicoAplicacao; // Recebe o novo serviço
    }

    private void validarFormulario() {
        if (formulario == null) {
             throw new IllegalArgumentException("O formulário não pode ser nulo.");
        }
        // REMOVIDO: A validação do jogadorId()
        // if (formulario.jogadorId() == null) { ... }
        if (formulario.nome() == null || formulario.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da premiação é obrigatório.");
        }
        if (formulario.dataPremiacao() == null) {
            throw new IllegalArgumentException("A data da premiação é obrigatória.");
        }
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            validarFormulario();

            // 1. LÓGICA DE NEGÓCIO: ENCONTRAR O JOGADOR DO MÊS
            Integer jogadorIdGanhador = notaServicoAplicacao.encontrarJogadorComMelhorNotaNoMes(
                formulario.dataPremiacao()
            );

            if (jogadorIdGanhador == null) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar um jogador com nota para o período. A premiação não pode ser criada.");
            }
            
            // 2. Cria os DadosPremiacao com o ID do jogador ganhador encontrado
            DadosPremiacao dados = new DadosPremiacao(
                jogadorIdGanhador, // ID determinado automaticamente
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
}