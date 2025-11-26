package dev.com.protactic.apresentacao.principal.feature_10_treino_tatico;

import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;
import dev.com.protactic.apresentacao.principal.feature_10_treino_tatico.SessaoTreinoControlador.SessaoTreinoFormulario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CriarSessaoComando implements ComandoSessaoTreino {

    private final SessaoTreinoService sessaoTreinoService;
    private final SessaoTreinoFormulario formulario;

    public CriarSessaoComando(SessaoTreinoService sessaoTreinoService, SessaoTreinoFormulario formulario) {
        this.sessaoTreinoService = sessaoTreinoService;
        this.formulario = formulario;
    }

    @Override
    public ResponseEntity<?> executar() {
        // Validação do Controller movida para o Command
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        if (formulario.clubeId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: ID do Clube é obrigatório.");
        }
        if (formulario.partidaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Partida não selecionada.");
        }
        if (formulario.nome() == null || formulario.nome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome da sessão é obrigatório.");
        }
        
        try {
            // 🎯 Chamada direta com os 4 parâmetros, evitando dependência de DTO de domínio
            sessaoTreinoService.criarSessaoPorIds(
                formulario.nome(),
                formulario.partidaId(),
                formulario.convocadosIds(),
                formulario.clubeId() 
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created

        } catch (Exception e) {
            // Tratamento de exceção movido para o Command
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}