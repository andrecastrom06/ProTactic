package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Date; 
// --- (INÍCIO DAS CORREÇÕES) ---
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // 1. Precisa disto para o JSON
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity; // 2. Precisa disto para a resposta (OK/Falha)
// Importa os SERVIÇOS DE APLICAÇÃO (Queries) do Clube
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos) e Repositórios
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService;

@RestController
@RequestMapping("backend/clube")
public class CadastroDeAtletaControlador {

    // --- Injeção dos Serviços ---
    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    private @Autowired CadastroDeAtletaService cadastroDeAtletaService;
    private @Autowired ClubeRepository clubeRepository;
    private @Autowired JogadorRepository jogadorRepository;
    
    
    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)

    @GetMapping(path = "pesquisa")
    public List<ClubeResumo> pesquisarResumos() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-competicao/{id}")
    public List<ClubeResumo> pesquisarResumosPorCompeticao(@PathVariable("id") Integer competicaoId) {
        return clubeServicoAplicacao.pesquisarResumosPorCompeticao(competicaoId);
    }

    
    // --- (INÍCIO DAS CORREÇÕES) ---
    // (Implementa a 'CadastroDeAtletaFeature' corretamente)

    /**
     * DTO/Formulário para receber os dados da contratação.
     * Agora inclui a DATA, como o BDD exige.
     * Exemplo de JSON:
     * {
     * "jogadorId": 2,
     * "data": "2025-07-15T12:00:00.000Z" 
     * }
     */
    public record ContratacaoFormulario(
        Integer jogadorId,
        Date data
    ) {}

    /**
     * Endpoint para POST /backend/clube/{clubeId}/contratar
     * (Corrigido para aceitar JSON e retornar uma resposta HTTP correta)
     */
    @PostMapping(path = "{clubeId}/contratar")
    public ResponseEntity<Void> contratarAtleta(
            @PathVariable("clubeId") Integer clubeId, 
            @RequestBody ContratacaoFormulario formulario) { // <-- Recebe o JSON
        
        // 1. Validar o formulário
        if (formulario == null || formulario.jogadorId() == null || formulario.data() == null) {
            throw new IllegalArgumentException("O formulário de contratação (com jogadorId e data) não pode ser nulo.");
        }

        // 2. Buscar as entidades de domínio completas
        Clube clube = clubeRepository.buscarPorId(clubeId);
        if (clube == null) {
            throw new RuntimeException("Clube não encontrado: " + clubeId);
        }

        Jogador jogador = jogadorRepository.buscarPorId(formulario.jogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + formulario.jogadorId());
        }

        // 3. Chamar o Serviço de DOMÍNIO (agora com a data correta)
        boolean resultado = cadastroDeAtletaService.contratar(clube, jogador, formulario.data());

        // 4. Retornar a resposta HTTP correta
        if (resultado) {
            // Sucesso! (Jogador contratado)
            return ResponseEntity.ok().build(); // Retorna 200 OK
        } else {
            // Falha! (Ex: Fora da janela, ou jogador já tem contrato)
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
    // --- (FIM DAS CORREÇÕES) ---
}