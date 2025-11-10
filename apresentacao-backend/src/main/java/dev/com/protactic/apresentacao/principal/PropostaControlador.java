package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Date; // Importar o Date
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import dev.com.protactic.aplicacao.principal.proposta.PropostaServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos) e Repositórios necessários
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.proposta.PropostaService;

@RestController
@RequestMapping("backend/proposta")
public class PropostaControlador {

    // --- Injeção dos Serviços ---
    private @Autowired PropostaServicoAplicacao propostaServicoAplicacao;
    private @Autowired PropostaService propostaService;
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired ClubeRepository clubeRepository;
    

    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)

    @GetMapping(path = "pesquisa")
    public List<PropostaResumo> pesquisarResumos() {
        return propostaServicoAplicacao.pesquisarResumos();
    }
    // ... (restante dos métodos GET) ...
    @GetMapping(path = "pesquisa-por-jogador/{jogadorId}")
    public List<PropostaResumo> pesquisarResumosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return propostaServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }
    @GetMapping(path = "pesquisa-por-propositor/{clubeId}")
    public List<PropostaResumo> pesquisarResumosPorPropositor(@PathVariable("clubeId") Integer clubeId) {
        return propostaServicoAplicacao.pesquisarResumosPorPropositor(clubeId);
    }
    @GetMapping(path = "pesquisa-por-receptor/{clubeId}")
    public List<PropostaResumo> pesquisarResumosPorReceptor(@PathVariable("clubeId") Integer clubeId) {
        return propostaServicoAplicacao.pesquisarResumosPorReceptor(clubeId);
    }


    // --- Endpoints de COMANDO (POST) ---

    public record PropostaFormulario(
        Integer jogadorId,
        Integer clubeId
    ) {}

    @PostMapping(path = "/criar")
    public void criarProposta(@RequestBody PropostaFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }

        // 1. Buscar as entidades de domínio completas
        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + formulario.jogadorId());
        }

        Clube clube = clubeRepository.buscarPorId(formulario.clubeId());
        if (clube == null) {
            throw new RuntimeException("Clube não encontrado: " + formulario.clubeId());
        }

        // --- (INÍCIO DA CORREÇÃO) ---
        // 2. O método 'criarProposta' pode lançar uma Exceção Verificada.
        //    Precisamos de a "apanhar" (catch) e "re-lançar" (throw)
        //    como uma RuntimeException.
        try {
            propostaService.criarProposta(jogador, clube, new Date());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar criar a proposta: " + e.getMessage(), e);
        }
        // --- (FIM DA CORREÇÃO) ---
    }
}
    