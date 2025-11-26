package dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna;

import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService.DadosPremiacao;
import dev.com.protactic.apresentacao.principal.feature_11_premiacao_interna.PremiacaoControlador.PremiacaoFormulario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class SalvarPremiacaoComando implements ComandoPremiacao {

    private final PremiacaoService servicoDominio;
    private final PremiacaoFormulario formulario;

    public SalvarPremiacaoComando(PremiacaoService servicoDominio, PremiacaoFormulario formulario) {
        this.servicoDominio = servicoDominio;
        this.formulario = formulario;
    }

    private void validarFormulario() {
        if (formulario == null) {
             throw new IllegalArgumentException("O formulário não pode ser nulo.");
        }
        if (formulario.jogadorId() == null) {
            throw new IllegalArgumentException("O ID do jogador é obrigatório.");
        }
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

            DadosPremiacao dados = new DadosPremiacao(
                formulario.jogadorId(),
                formulario.nome(),
                formulario.dataPremiacao()
            );

            Premiacao premiacaoSalva = servicoDominio.salvarPremiacaoPorDados(dados);
            
            return ResponseEntity.ok(premiacaoSalva); 

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}