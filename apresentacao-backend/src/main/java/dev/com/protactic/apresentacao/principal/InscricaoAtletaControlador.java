package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaServicoAplicacao;
import dev.com.protactic.dominio.principal.InscricaoAtleta;
import dev.com.protactic.dominio.principal.Jogador;

// --- (INÍCIO DAS MUDANÇAS) ---
// Importamos o seu NOVO serviço de domínio e os repositórios que ele precisa
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoService;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
// --- (FIM DAS MUDANÇAS) ---

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("backend/inscricao")
@CrossOrigin(origins = "http://localhost:3000")

public class InscricaoAtletaControlador {

    // --- (INJEÇÕES ATUALIZADAS) ---
    
    // Serviço para consultas (GET) - (Isto não muda)
    private @Autowired InscricaoAtletaServicoAplicacao servicoAplicacao;
    
    // O SEU serviço de domínio com a lógica de negócio (POST)
    private @Autowired RegistroInscricaoService servicoDominio;

    // Repositórios necessários para encontrar os dados
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired RegistroLesoesRepository lesoesRepository;

    // --- (FIM DAS INJEÇÕES) ---


    /**
     * DTO (Formulário) para o POST.
     * Só precisamos do nome do atleta e da competição.
     */
    public record InscricaoFormulario(
        String atleta,
        String competicao
    ) {}

    /**
     * POST /backend/inscricao/salvar
     * Roda a lógica de negócio de inscrição.
     */
    @PostMapping("/salvar")
    public InscricaoAtleta salvarInscricao(@RequestBody InscricaoFormulario formulario) {
        
        // 1. Buscar os dados que faltam para o serviço
        
        // (Assume que o jogadorRepository tem o findByNomeIgnoreCase que fizemos para os cartões)
        Jogador jogador = jogadorRepository.findByNomeIgnoreCase(formulario.atleta())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Jogador " + formulario.atleta() + " não encontrado"));
        
        int idade = jogador.getIdade(); // <-- Encontrámos a idade
        
        // (Assume que o lesoesRepository tem este método, como usado no EscalacaoControlador)
        boolean contratoAtivo = lesoesRepository.contratoAtivo(formulario.atleta()); // <-- Encontrámos o contrato

        // 2. Chamar o SEU serviço de domínio com todos os dados
        return servicoDominio.registrarInscricao(
            formulario.atleta(),
            idade,
            contratoAtivo,
            formulario.competicao()
        );
    }

    // --- (OS MÉTODOS GET CONTINUAM IGUAIS) ---

    @GetMapping("/todos")
    public List<InscricaoAtletaResumo> pesquisarTodos() {
        return servicoAplicacao.pesquisarResumos();
    }

    @GetMapping("/atleta/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorAtleta(@PathVariable("nome") String atleta) {
        return servicoAplicacao.pesquisarResumosPorAtleta(atleta);
    }

    @GetMapping("/competicao/{nome}")
    public List<InscricaoAtletaResumo> pesquisarPorCompeticao(@PathVariable("nome") String competicao) {
        return servicoAplicacao.pesquisarResumosPorCompeticao(competicao);
    }
}