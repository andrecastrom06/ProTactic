package dev.com.protactic.apresentacao.principal;

// Importações do Spring
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa o SERVIÇO DE APLICAÇÃO (Queries) de Jogador
import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;
import dev.com.protactic.aplicacao.principal.jogador.JogadorServicoAplicacao;

// Importa os serviços/repositórios de Clube e Capitão
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;
import dev.com.protactic.dominio.principal.Capitao;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos) e Repositórios
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoService;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

@RestController
@RequestMapping("backend/capitao") 
public class DefinirCapitaoControlador { 

    // --- Injeção dos Serviços ---
    private @Autowired JogadorServicoAplicacao jogadorServicoAplicacao;
    private @Autowired CapitaoService capitaoService;
    private @Autowired JogadorRepository jogadorRepository; 
    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    private @Autowired CapitaoRepository capitaoRepository;
    
    // --- Endpoints de CONSULTA (GET) ---

    @GetMapping(path = "pesquisa-jogadores")
    public List<JogadorResumo> pesquisarResumosDeJogadores() {
        return jogadorServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-jogadores-por-clube/{clubeId}")
    public List<JogadorResumo> pesquisarResumosDeJogadoresPorClube(@PathVariable("clubeId") Integer clubeId) {
        return jogadorServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }
    
    @GetMapping(path = "buscar-por-clube/{clubeId}")
    public Capitao buscarCapitaoDoClube(@PathVariable("clubeId") Integer clubeId) {
        return capitaoRepository.buscarCapitaoPorClube(clubeId);
    }

    @GetMapping(path = "listar-todos")
    public List<ClubeResumo> listarTodosOsCapitaes() {
        return clubeServicoAplicacao.pesquisarResumos();
    }
    
    // --- Endpoints de COMANDO (POST) ---

    /**
     * Endpoint para POST /backend/capitao/definir/{jogadorId}
     * (Este é o seu 'definirCapitao' antigo)
     */
    // --- (INÍCIO DA CORREÇÃO) ---
    @PostMapping(path = "definir/{jogadorId}") // <-- Faltava o '=' 
    // --- (FIM DA CORREÇÃO) ---
    public void definirCapitao(@PathVariable("jogadorId") Integer jogadorId) {
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + jogadorId);
        }
        
        capitaoService.definirCapitao(jogador);
    }
}