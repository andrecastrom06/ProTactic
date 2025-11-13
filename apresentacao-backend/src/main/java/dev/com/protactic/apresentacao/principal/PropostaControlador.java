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

import org.springframework.http.ResponseEntity; // <-- 1. Adiciona import
import org.springframework.web.bind.annotation.DeleteMapping; // <-- 2. Adiciona import
import org.springframework.web.bind.annotation.PatchMapping;

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

    /**
     * Formulário (DTO) que a API recebe para criar uma proposta.
     * Agora inclui o 'valor'.
     */
    public record PropostaFormulario(
        Integer jogadorId,
        Integer clubeId,
        double valor // <-- CORREÇÃO: Campo de valor adicionado
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

        // O 'clubeId' do formulário é o clube que FAZ a proposta (Propositor)
        Clube clubePropositor = clubeRepository.buscarPorId(formulario.clubeId());
        if (clubePropositor == null) {
            throw new RuntimeException("Clube propositor não encontrado: " + formulario.clubeId());
        }

        // 2. Chama o serviço de domínio, passando o valor do formulário
        try {
            propostaService.criarProposta(
                jogador, 
                clubePropositor, 
                formulario.valor(), // <-- CORREÇÃO: Passa o valor
                new Date()
            );
        } catch (Exception e) {
            // Re-lança a exceção do domínio como uma exceção de runtime
            throw new RuntimeException("Erro ao tentar criar a proposta: " + e.getMessage(), e);
        }
    }
    
    public record PropostaValorFormulario(
        double novoValor
    ) {}

    /**
     * PATCH /backend/proposta/editar-valor/{propostaId}
     * Implementa a história "Editar proposta já criada".
     */
    @PatchMapping(path = "/editar-valor/{propostaId}")
    public ResponseEntity<Void> editarValorProposta(
            @PathVariable("propostaId") Integer propostaId,
            @RequestBody PropostaValorFormulario formulario) {
        try {
            propostaService.editarValorProposta(propostaId, formulario.novoValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar proposta: " + e.getMessage(), e);
        }
    }

    /**
     * POST /backend/proposta/aceitar/{propostaId}
     * Implementa a história "Aceitar proposta".
     */
    @PostMapping(path = "/aceitar/{propostaId}")
    public ResponseEntity<Void> aceitarProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.aceitarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao aceitar proposta: " + e.getMessage(), e);
        }
    }

    /**
     * POST /backend/proposta/recusar/{propostaId}
     * Implementa a história "Recusar proposta".
     */
    @PostMapping(path = "/recusar/{propostaId}")
    public ResponseEntity<Void> recusarProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.recusarProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recusar proposta: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE /backend/proposta/excluir/{propostaId}
     * Implementa a história "Excluir proposta enviada".
     */
    @DeleteMapping(path = "/excluir/{propostaId}")
    public ResponseEntity<Void> excluirProposta(@PathVariable("propostaId") Integer propostaId) {
        try {
            propostaService.excluirProposta(propostaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir proposta: " + e.getMessage(), e);
        }
    }
}