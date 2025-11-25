package dev.com.protactic.apresentacao.principal.feature_02_carga_semanal;

import dev.com.protactic.dominio.principal.Fisico;
import dev.com.protactic.dominio.principal.planejamentoFisico.PlanejamentoFisicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.com.protactic.apresentacao.principal.feature_02_carga_semanal.TreinoFisicoControlador.TreinoFisicoFormulario;

public class CriarProtocoloComando implements ComandoTreinoFisico {

    private final PlanejamentoFisicoService planejamentoFisicoService;
    private final Integer jogadorId;
    private final TreinoFisicoFormulario formulario;

    public CriarProtocoloComando(
            PlanejamentoFisicoService planejamentoFisicoService,
            Integer jogadorId,
            TreinoFisicoFormulario formulario) {
        this.planejamentoFisicoService = planejamentoFisicoService;
        this.jogadorId = jogadorId;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}