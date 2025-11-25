package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;
import dev.com.protactic.aplicacao.principal.fisico.FisicoServicoAplicacao;
import dev.com.protactic.dominio.principal.Fisico;
import dev.com.protactic.dominio.principal.planejamentoFisico.PlanejamentoFisicoService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("backend/treino-fisico")
@CrossOrigin(origins = "http://localhost:3000")
public class TreinoFisicoControlador {

    @Autowired private FisicoServicoAplicacao fisicoServicoAplicacao;
    
    @Autowired private PlanejamentoFisicoService planejamentoFisicoService; 

    @GetMapping("/por-jogador/{jogadorId}")
    public List<FisicoResumo> buscarTreinosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return fisicoServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }

    public record TreinoFisicoFormulario(
        String nome,
        String musculo,
        String intensidade,
        String descricao,
        Date dataInicio,
        Date dataFim
    ) {}

    @PostMapping("/salvar/{jogadorId}")
    public ResponseEntity<?> salvarTreinoFisico(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody TreinoFisicoFormulario formulario) {
        
        ComandoTreinoFisico comando = new SalvarTreinoFisicoComando(
            planejamentoFisicoService,
            jogadorId,
            formulario
        );
        
        return comando.executar();
    }
    
    @PutMapping("/editar/{treinoId}")
    public ResponseEntity<?> editarTreinoFisico(
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody TreinoFisicoFormulario formulario) {
        try {
             PlanejamentoFisicoService.DadosTreinoFisico dados = 
                new PlanejamentoFisicoService.DadosTreinoFisico(
                    null, 
                    formulario.nome(),
                    formulario.musculo(),
                    formulario.intensidade(),
                    formulario.descricao(),
                    formulario.dataInicio(),
                    formulario.dataFim()
                );
                
            Fisico treinoAtualizado = planejamentoFisicoService.editarTreinoFisico(treinoId, dados);
            return ResponseEntity.ok(treinoAtualizado);
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public record StatusUpdateFormulario(String status) {}

    @PatchMapping("/atualizar-status/{treinoId}")
    public ResponseEntity<?> atualizarStatusTreino(
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody StatusUpdateFormulario formulario) {
        try {
            Fisico treinoAtualizado = planejamentoFisicoService.atualizarStatusTreino(treinoId, formulario.status());
            return ResponseEntity.ok(treinoAtualizado);
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping("/criar-protocolo-retorno/{jogadorId}")
    public ResponseEntity<?> criarProtocoloDeRetorno(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody TreinoFisicoFormulario formulario) {
         
        ComandoTreinoFisico comando = new CriarProtocoloComando(
            planejamentoFisicoService,
            jogadorId,
            formulario
        );
        
        return comando.executar();
    }
}