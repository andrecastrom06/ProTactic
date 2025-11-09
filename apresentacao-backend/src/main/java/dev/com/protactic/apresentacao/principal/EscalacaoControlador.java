/** package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos)
import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;

// Importa os Repositórios e Serviços de que precisamos
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;

// --- (INÍCIO DA CORREÇÃO) ---
// Precisamos de importar a entidade 'Suspensao' para verificar o seu estado
import dev.com.protactic.dominio.principal.Suspensao;
// --- (FIM DA CORREÇÃO) ---


@RestController
@RequestMapping("backend/escalacao")
public class EscalacaoControlador {

    // --- Injeção dos Serviços ---
    private @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService;
    private @Autowired RegistroLesoesRepository registroLesoesRepository;
    private @Autowired RegistroCartoesService registroCartoesService;
    
    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)
    @GetMapping(path = "pesquisa-por-data/{jogoData}")
    public List<EscalacaoResumo> pesquisarResumosPorData(@PathVariable("jogoData") String jogoData) {
        return escalacaoServicoAplicacao.pesquisarResumosPorData(jogoData);
    }
    @GetMapping(path = "obter-por-data/{jogoData}")
    public List<String> obterEscalacao(@PathVariable("jogoData") String jogoData) {
        return definirEsquemaTaticoService.obterEscalacao(jogoData);
    }

    // --- Endpoints de COMANDO (POST) ---

    public record EscalacaoFormulario(
        String jogoData,
        String nomeJogador
    ) {}

    @PostMapping(path = "/registrar")
    public void registrarEscalacao(@RequestBody EscalacaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }

        // --- (INÍCIO DAS CORREÇÕES) ---
        // 1. Precisamos de buscar os 3 argumentos que faltam
        String atletaId = formulario.nomeJogador(); 

        // Argumento 1: contratoAtivo
        boolean contratoAtivo = registroLesoesRepository.contratoAtivo(atletaId);

        // Argumento 2: suspenso
        // O serviço retorna o objeto 'Suspensao' completo
        Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(atletaId);
        
        // Verificamos o estado 'isSuspenso()' dentro do objeto.
        // Se o objeto for nulo (jogador sem registro de suspensão), assumimos que não está suspenso.
        boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;


        // Argumento 3: grauLesao
        int grauLesao = registroLesoesRepository.grauLesaoAtiva(atletaId).orElse(0);


        // 2. Chama o Serviço de DOMÍNIO com os 5 argumentos corretos
        try {
            definirEsquemaTaticoService.registrarEscalacao(
                formulario.jogoData(),
                atletaId,
                contratoAtivo,
                suspenso,
                grauLesao
            );
        } catch (Exception e) {
            // Embrulha qualquer exceção verificada
            throw new RuntimeException("Erro ao registrar escalação: " + e.getMessage(), e);
        }
        // --- (FIM DAS CORREÇÕES) ---
    }
} */