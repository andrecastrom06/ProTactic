package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoServicoAplicacao;

// Importa o SERVIÇO DE DOMÍNIO
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;

@RestController
@RequestMapping("backend/registro-cartoes")
public class RegistroCartoesControlador {

    private @Autowired RegistroCartaoServicoAplicacao registroCartaoServicoAplicacao;
    private @Autowired RegistroCartoesService registroCartoesService;

    // --- Endpoints de CONSULTA (GET) ---

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


    // --- Endpoints de COMANDO (POST) ---

    public record CartaoFormulario(
        String atleta,
        String tipo
    ) {}

    @PostMapping(path = "/registrar")
    public void registrarCartao(@RequestBody CartaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        try {
            registroCartoesService.registrarCartao(formulario.atleta(), formulario.tipo());
        } catch (Exception e) {
            // --- (INÍCIO DA MODIFICAÇÃO PARA DEBUG) ---
            // Adicionamos estas linhas para vermos o erro no terminal
            System.err.println("### ERRO AO REGISTRAR CARTÃO ###");
            e.printStackTrace(); 
            // --- (FIM DA MODIFICAÇÃO PARA DEBUG) ---
            
            throw new RuntimeException("Erro ao registrar cartão: " + e.getMessage(), e);
        }
    }

    @PostMapping(path = "/limpar/{atleta}")
    public void limparCartoes(@PathVariable("atleta") String atleta) {
        
        try {
            registroCartoesService.limparCartoes(atleta);
        } catch (Exception e) {
            // --- (INÍCIO DA MODIFICAÇÃO PARA DEBUG) ---
            System.err.println("### ERRO AO LIMPAR CARTÕES ###");
            e.printStackTrace();
            // --- (FIM DA MODIFICAÇÃO PARA DEBUG) ---
            
            throw new RuntimeException("Erro ao limpar cartões: " + e.getMessage(), e);
        }
    }
} 