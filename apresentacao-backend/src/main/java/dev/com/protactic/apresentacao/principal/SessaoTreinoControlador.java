package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.ArrayList; 
import java.util.Objects; // <-- 1. IMPORTAMOS O 'OBJECTS'
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos)
import dev.com.protactic.dominio.principal.SessaoTreino; 
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoService;

// Importa os Repositórios e Mapeadores de que precisamos
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// Corrigido para o pacote 'partida'
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaRepositorySpringData;


@RestController
@RequestMapping("backend/sessao-treino")
@CrossOrigin(origins = "http://localhost:3000")
public class SessaoTreinoControlador {

    // --- Injeção dos Serviços ---
    private @Autowired SessaoTreinoServicoAplicacao sessaoTreinoServicoAplicacao;
    private @Autowired SessaoTreinoService sessaoTreinoService;
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired PartidaRepositorySpringData partidaRepositoryJPA;
    private @Autowired JpaMapeador mapeador;
    

    // --- Endpoints de CONSULTA (GET) ---
    // (Omitido para brevidade)
    @GetMapping(path = "pesquisa")
    public List<SessaoTreinoResumo> pesquisarResumos() {
        return sessaoTreinoServicoAplicacao.pesquisarResumos();
    }
    @GetMapping(path = "pesquisa-por-partida/{partidaId}")
    public List<SessaoTreinoResumo> pesquisarResumosPorPartida(@PathVariable("partidaId") Integer partidaId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorPartida(partidaId);
    }
    @GetMapping(path = "pesquisa-por-convocado/{jogadorId}")
    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(@PathVariable("jogadorId") Integer jogadorId) {
        return sessaoTreinoServicoAplicacao.pesquisarResumosPorConvocado(jogadorId);
    }
    @GetMapping(path = "listar-por-nome-partida/{partidaNome}")
    public List<SessaoTreino> listarSessoesPorPartida(@PathVariable("partidaNome") String partidaNome) {
        return sessaoTreinoService.listarSessoesPorPartida(partidaNome);
    }


    // --- Endpoints de COMANDO (POST) ---

    public record SessaoTreinoFormulario(
        String nome,
        Integer partidaId,
        List<Integer> convocadosIds
    ) {}

    @PostMapping(path = "/criar")
    public void criarSessao(@RequestBody SessaoTreinoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        // --- (INÍCIO DA CORREÇÃO) ---
        // 2. Validamos o ID da partida ANTES de o usar
        Objects.requireNonNull(formulario.partidaId(), "O 'partidaId' no formulário não pode ser nulo.");
        // --- (FIM DA CORREÇÃO) ---

        // 3. Buscar o objeto Partida
        // (O aviso amarelo deve ter desaparecido da linha abaixo)
        Integer partidaId = formulario.partidaId();
        Objects.requireNonNull(partidaId, "O 'partidaId' no formulário não pode ser nulo.");

        Partida partida = partidaRepositoryJPA.findById(partidaId)
            .map(partidaJPA -> mapeador.map(partidaJPA, Partida.class))
            .orElse(null); 

        if (partida == null) {
            throw new RuntimeException("Partida não encontrada: " + formulario.partidaId());
        }

        // 4. Buscar a Lista<Jogador>
        List<Jogador> jogadores = new ArrayList<>();
        if (formulario.convocadosIds() != null) {
            for (Integer jogadorId : formulario.convocadosIds()) {
                Jogador j = jogadorRepository.buscarPorId(jogadorId);
                if (j != null) {
                    jogadores.add(j);
                } else {
                    System.err.println("Aviso: Jogador com ID " + jogadorId + " não encontrado ao criar sessão.");
                }
            }
        }
        
        // 5. Chamar o serviço com os OBJETOS
        try {
            sessaoTreinoService.criarSessao(
                formulario.nome(),
                partida,
                jogadores
            );
        } catch (Exception e) {
            // --- INÍCIO DA CORREÇÃO ---
            // FORÇA O ERRO A SER IMPRESSO NO TERMINAL
            System.err.println("### OCORREU UM ERRO GRAVE ###");
            e.printStackTrace(); // Isto vai imprimir o erro a vermelho
            // --- FIM DA CORREÇÃO ---
            
            // Re-lança a exceção
            throw new RuntimeException("Erro ao criar sessão de treino: " + e.getMessage(), e);
        }
    }
} 