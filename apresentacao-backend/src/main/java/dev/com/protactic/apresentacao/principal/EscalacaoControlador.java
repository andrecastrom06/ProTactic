package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoServicoAplicacao;

import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;

import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
import dev.com.protactic.dominio.principal.Suspensao; 

@RestController
@RequestMapping("backend/escalacao")
@CrossOrigin(origins = "http://localhost:3000")

public class EscalacaoControlador {

    private @Autowired EscalacaoServicoAplicacao escalacaoServicoAplicacao;
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService;
    private @Autowired RegistroLesoesRepository registroLesoesRepository;
    private @Autowired RegistroCartoesService registroCartoesService;
    
    
    @GetMapping(path = "pesquisa-por-data/{jogoData}")
    public List<EscalacaoResumo> pesquisarResumosPorData(@PathVariable("jogoData") String jogoData) {
        return escalacaoServicoAplicacao.pesquisarResumosPorData(jogoData);
    }
    
    @GetMapping(path = "obter-por-data/{jogoData}")
    public List<String> obterEscalacao(@PathVariable("jogoData") String jogoData) {
        return definirEsquemaTaticoService.obterEscalacao(jogoData);
    }


    public record EscalacaoFormulario(
        String jogoData,
        String nomeJogador
    ) {}

    @PostMapping(path = "/registrar")
    public void registrarEscalacao(@RequestBody EscalacaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }

        String atletaId = formulario.nomeJogador(); 
        boolean contratoAtivo = registroLesoesRepository.contratoAtivo(atletaId);
        Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(atletaId);
        boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;
        int grauLesao = registroLesoesRepository.grauLesaoAtiva(atletaId).orElse(0);

        try {
            definirEsquemaTaticoService.registrarEscalacao(
                formulario.jogoData(),
                atletaId,
                contratoAtivo,
                suspenso,
                grauLesao
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar escalação: " + e.getMessage(), e);
        }
    }
}