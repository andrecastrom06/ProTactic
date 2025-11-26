package dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao;

import dev.com.protactic.dominio.principal.proposta.PropostaService;
import dev.com.protactic.dominio.principal.proposta.PropostaService.DadosNovaProposta;
import dev.com.protactic.apresentacao.principal.feature_05_proposta_contratacao.PropostaControlador.PropostaFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;

public class CriarPropostaComando implements ComandoProposta {

    private final PropostaService propostaService;
    private final PropostaFormulario formulario;

    public CriarPropostaComando(PropostaService propostaService, PropostaFormulario formulario) {
        this.propostaService = propostaService;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        if (formulario == null) {
            return ResponseEntity.badRequest().body("O formulário não pode ser nulo.");
        }
        
        try {
            DadosNovaProposta dados = new DadosNovaProposta(
                formulario.jogadorId(),
                formulario.clubeId(),
                formulario.valor(),
                new Date()
            );
            
            propostaService.criarPropostaPorIds(dados);
            
            return ResponseEntity.status(HttpStatus.CREATED).build(); 

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar proposta: " + e.getMessage());
        }
    }
}