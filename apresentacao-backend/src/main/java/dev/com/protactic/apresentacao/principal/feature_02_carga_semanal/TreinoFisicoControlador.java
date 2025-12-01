package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;
import dev.com.protactic.aplicacao.principal.fisico.FisicoServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.Fisico;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoFisicoService;

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
        
        // 🎯 LÓGICA DO SalvarTreinoFisicoComando MOVIDA PARA CÁ
        try {
            PlanejamentoFisicoService.DadosTreinoFisico dados =
                new PlanejamentoFisicoService.DadosTreinoFisico(
                    jogadorId,
                    formulario.nome(),
                    formulario.musculo(),
                    formulario.intensidade(),
                    formulario.descricao(),
                    formulario.dataInicio(),
                    formulario.dataFim()
                );

            Fisico treinoSalvo = planejamentoFisicoService.salvarTreinoFisico(dados);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(treinoSalvo);

        } catch (Exception e) {
            // O tratamento de exceção do comando foi movido para cá.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
         
        // 🎯 LÓGICA DO CriarProtocoloComando MOVIDA PARA CÁ
        try {
            PlanejamentoFisicoService.DadosTreinoFisico dados =
                new PlanejamentoFisicoService.DadosTreinoFisico(
                    jogadorId,
                    formulario.nome(),
                    formulario.musculo(),
                    formulario.intensidade(),
                    formulario.descricao(),
                    formulario.dataInicio(),
                    formulario.dataFim()
                );
            
            Fisico protocoloSalvo = planejamentoFisicoService.criarProtocoloDeRetorno(dados);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(protocoloSalvo);

        } catch (Exception e) {
            // O tratamento de exceção do comando foi movido para cá.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}