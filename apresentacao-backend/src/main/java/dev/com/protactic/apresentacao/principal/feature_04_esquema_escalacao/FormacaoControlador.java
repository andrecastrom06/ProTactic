package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; 
import org.springframework.beans.factory.annotation.Autowired; // Adicionado para injeção

@RestController
@RequestMapping("/backend/formacao")
@CrossOrigin(origins = "http://localhost:3000")
public class FormacaoControlador {

    // A injeção via campo ou construtor é preferível
    private final FormacaoServicoAplicacao formacaoServicoAplicacao;

    @Autowired // Mantido o construtor, assumindo que Spring faz a injeção
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
            
            // 🎯 USO DO BUILDER: Criação do DTO de forma encadeada e validada
            var dadosServico = FormacaoDadosBuilder.fromFormulario(form)
                                                   .validarClubeId() // Executa validação específica
                                                   .build();         // Constrói o DTO final

            FormacaoResumo resumo = formacaoServicoAplicacao.salvarFormacao(dadosServico);
            return ResponseEntity.ok(resumo);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura erros lançados pelo Builder (validarClubeId, build)
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