package dev.com.protactic.apresentacao.principal.feature_08_registro_cartoes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; // 🎯 NOVO IMPORT

import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade.Suspensao;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.servico.RegistroCartoesService; 


@RestController
@RequestMapping("backend/registro-cartoes")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistroCartoesControlador {

    @Autowired 
    private RegistroCartaoServicoAplicacao registroCartaoServicoAplicacao;
    
    @Autowired 
    private RegistroCartoesService registroCartoesService;

    @GetMapping(path = "pesquisa")
    public List<RegistroCartaoResumo> pesquisarResumos() {
        return registroCartaoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-atleta/{atleta}")
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(@PathVariable("atleta") String atleta) {
        return registroCartaoServicoAplicacao.pesquisarResumosPorAtleta(atleta);
    }
    
    @GetMapping(path = "pesquisa-por-tipo/{tipo}")
    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(@PathVariable("tipo") String tipo) {
        return registroCartaoServicoAplicacao.pesquisarResumosPorTipo(tipo);
    }

    @GetMapping(path = "/suspensoes/{clubeId}")
    public List<Suspensao> listarSuspensosPorClube(@PathVariable("clubeId") Integer clubeId) {
        return registroCartoesService.listarSuspensosPorClube(clubeId);
    }

    public record CartaoFormulario(
        String atleta,
        String tipo
    ) {}

    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrarCartao(@RequestBody CartaoFormulario formulario) {
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("O corpo da requisição não pode ser nulo.");
        }
        
        try {
            registroCartoesService.registrarCartao(formulario.atleta(), formulario.tipo());
            
            return ResponseEntity.ok().build(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar cartão: " + e.getMessage());
        }
    }

    @PostMapping(path = "/limpar/{atleta}")
    public ResponseEntity<?> limparCartoes(@PathVariable("atleta") String atleta) {
        
        try {
            registroCartoesService.limparCartoes(atleta);
            
            return ResponseEntity.ok().build(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao limpar cartões: " + e.getMessage());
        }
    }
}