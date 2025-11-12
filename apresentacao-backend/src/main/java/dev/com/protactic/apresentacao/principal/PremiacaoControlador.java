package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoServicoAplicacao;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoRepository;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("backend/premiacao")
public class PremiacaoControlador {

    // Serviço para consultas (GET)
    private @Autowired PremiacaoServicoAplicacao servicoAplicacao;
    
    // Repositório de domínio para comandos (POST)
    private @Autowired PremiacaoRepository repositorioDominio;

    // Repositório para buscar o objeto Jogador
    private @Autowired JogadorRepository jogadorRepository;

    /**
     * DTO (Formulário) para criar uma nova premiação.
     */
    public record PremiacaoFormulario(
        Integer jogadorId,
        String nome,
        @JsonFormat(pattern = "yyyy-MM-dd") Date dataPremiacao
    ) {}

    /**
     * REQUISITO 1: Criar uma premiação para um jogador
     * POST /backend/premiacao/salvar
     */
    @PostMapping("/salvar")
    public void salvarPremiacao(@RequestBody PremiacaoFormulario formulario) {
        
        if (formulario.jogadorId() == null) {
            throw new IllegalArgumentException("O 'jogadorId' é obrigatório.");
        }
        
        // 1. Buscar o objeto Jogador completo usando o ID
        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador com ID " + formulario.jogadorId() + " não encontrado.");
        }
        
        // 2. Cria o objeto de Domínio com o objeto Jogador
        Premiacao novoPremio = new Premiacao(
            0,
            jogador, // Passamos o objeto completo
            formulario.nome(),
            formulario.dataPremiacao()
        );

        // 3. Salva o prémio no banco
        repositorioDominio.salvarPremiacao(novoPremio);
    }

    /**
     * REQUISITO 2: Buscar todas as premiações
     * GET /backend/premiacao/todos
     */
    @GetMapping("/todos")
    public List<PremiacaoResumo> pesquisarTodos() {
        return servicoAplicacao.pesquisarResumos();
    }

    /**
     * REQUISITO 3: Buscar por ID do jogador
     * GET /backend/premiacao/jogador/{id}
     */
    @GetMapping("/jogador/{id}")
    public List<PremiacaoResumo> pesquisarPorJogador(@PathVariable("id") Integer jogadorId) {
        return servicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }

    /**
     * REQUISITO 4: Buscar por nome da premiação
     * GET /backend/premiacao/nome/{nome}
     */
    @GetMapping("/nome/{nome}")
    public List<PremiacaoResumo> pesquisarPorNome(@PathVariable("nome") String nome) {
        return servicoAplicacao.pesquisarResumosPorNome(nome);
    }
}