package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao.FormacaoDados; // 🎯 IMPORT ADICIONADO

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; 


@RestController
@RequestMapping("/backend/formacao")
@CrossOrigin(origins = "http://localhost:3000")
public class FormacaoControlador {

    private final FormacaoServicoAplicacao formacaoServicoAplicacao;

    
    public FormacaoControlador(FormacaoServicoAplicacao formacaoServicoAplicacao) {
        this.formacaoServicoAplicacao = formacaoServicoAplicacao;
    }


    public record FormacaoFormulario(
        Integer partidaId, 
        String esquema, 
        List<Integer> jogadoresIds,
        Integer clubeId
    ) {}

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarFormacao(@RequestBody FormacaoFormulario form) {
        try {
            
            // 🎯 LÓGICA DO BUILDER MOVIDA PARA CÁ (Criação e Validação)
            
            // 1. Validação do clubeId (vinda de validarClubeId())
            if (form.clubeId() == null) {
                throw new IllegalArgumentException("Erro de segurança: ID do clube não informado.");
            }
            
            // 2. Validação dos campos obrigatórios (vinda de build())
            if (form.esquema() == null || form.jogadoresIds() == null) {
                throw new IllegalStateException("Esquema tático e jogadores são obrigatórios.");
            }
            
            // 3. Criação direta do DTO FormacaoDados
            FormacaoDados dadosServico = new FormacaoDados(
                form.partidaId(),
                form.esquema(),
                form.jogadoresIds(),
                form.clubeId()
            );

            // ----------------------------------------------------

            FormacaoResumo resumo = formacaoServicoAplicacao.salvarFormacao(dadosServico);
            return ResponseEntity.ok(resumo);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura erros lançados pelas validações movidas
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-por-partida/{partidaId}")
    public ResponseEntity<?> buscarPorPartida(@PathVariable Integer partidaId, @RequestParam(required = false) Integer clubeId) {
        
        Optional<FormacaoResumo> resumo = formacaoServicoAplicacao.buscarPorPartida(partidaId);
        
        if (resumo.isPresent()) {
             return ResponseEntity.ok(resumo.get());
        } else {
             return ResponseEntity.noContent().build();
        }
    }
}