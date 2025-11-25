package dev.com.protactic.apresentacao.principal.feature_01_cadastro_atleta;

import java.util.List;
import java.util.Date; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpStatus; 

import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;

import dev.com.protactic.apresentacao.principal.feature_01_cadastro_atleta.CadastroContratacaoFacade.ContratacaoNaoPermitidaException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/cadastro-atleta")
public class CadastroDeAtletaControlador {

    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    
    private @Autowired CadastroContratacaoFacade cadastroContratacaoFacade; 

    @GetMapping(path = "pesquisa")
    public List<ClubeResumo> pesquisarResumos() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-competicao/{id}")
    public List<ClubeResumo> pesquisarResumosPorCompeticao(@PathVariable("id") Integer competicaoId) {
        return clubeServicoAplicacao.pesquisarResumosPorCompeticao(competicaoId);
    }

    
    public record ContratacaoFormulario(
        Integer jogadorId,
        Date data
    ) {}

    /**
     * Endpoint de Contratação que utiliza o Padrão Façade para simplificar a lógica.
     */
    @PostMapping(path = "{clubeId}/contratar")
    public ResponseEntity<?> contratarAtleta( 
            @PathVariable("clubeId") Integer clubeId, 
            @RequestBody ContratacaoFormulario formulario) { 
        
        // Validação de entrada (mantida no Controlador)
        if (formulario == null || formulario.jogadorId() == null || formulario.data() == null) {
            return ResponseEntity
                .badRequest()
                .body("O formulário de contratação (com jogadorId e data) não pode ser nulo.");
        }

        try {
            // 🎯 USO DO FACADE: O Controlador chama a Façade.
            // A Façade orquestra o serviço de Domínio e faz o tratamento da regra de negócio.
            cadastroContratacaoFacade.processarContratacao(
                clubeId, 
                formulario.jogadorId(), 
                formulario.data()
            );

            return ResponseEntity.ok().build(); 

        } catch (ContratacaoNaoPermitidaException e) {
            // A Façade lançou esta exceção por falha na regra de negócio (Contratação não permitida).
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403 Forbidden
        } catch (Exception e) {
            // Outras exceções (ex: ID não encontrado)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
  
}