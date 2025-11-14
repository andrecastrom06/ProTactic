package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // <-- 1. Adiciona import
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // <-- 2. Adiciona import
import org.springframework.web.bind.annotation.RequestBody; // <-- 3. Adiciona import
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;
import dev.com.protactic.aplicacao.principal.contrato.ContratoServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos) e Repositórios necessários
import dev.com.protactic.dominio.principal.Contrato; // <-- 4. Adiciona import
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.dispensa.DispensaService;
import dev.com.protactic.dominio.principal.contrato.ContratoService; // <-- 5. Adiciona import

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/contrato")
public class ContratoControlador { // <-- Classe renomeada

    // --- Injeção dos Serviços ---

    private @Autowired ContratoServicoAplicacao contratoServicoAplicacao;
    private @Autowired DispensaService dispensaService;
    private @Autowired JogadorRepository jogadorRepository;
    
    // --- (INÍCIO DA NOVA FUNCIONALIDADE) ---
    private @Autowired ContratoService contratoService; // <-- 6. Injeta o novo serviço
    // --- (FIM DA NOVA FUNCIONALIDADE) ---
    

    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)

    @GetMapping(path = "pesquisa")
    public List<ContratoResumo> pesquisarResumos() {
        return contratoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-clube/{clubeId}")
    public List<ContratoResumo> pesquisarResumosPorClube(@PathVariable("clubeId") Integer clubeId) {
        return contratoServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }

    
    // --- Endpoints de COMANDO (POST) ---
  
    @PostMapping(path = "/dispensar/{jogadorId}")
    public void dispensarJogador(@PathVariable("jogadorId") Integer jogadorId) {
        
        // ... (Este método continua igual)
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + jogadorId);
        }
        try {
            dispensaService.dispensarJogador(jogador);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar dispensar o jogador: " + e.getMessage(), e);
        }
    }

    // --- (INÍCIO DA NOVA FUNCIONALIDADE "RENOVAR") ---

    /**
     * DTO (Formulário) para renovar um contrato.
     * Ex: { "duracaoMeses": 36, "salario": 150000.0, "status": "ATIVO" }
     */
    public record RenovacaoFormulario(
        int duracaoMeses,
        double salario,
        String status
    ) {}

    /**
     * PUT /backend/contrato/renovar/{contratoId}
     * Implementa a história "Atualizar contratos".
     */
    @PutMapping(path = "/renovar/{contratoId}")
    public ResponseEntity<Contrato> renovarContrato(
            @PathVariable("contratoId") Integer contratoId,
            @RequestBody RenovacaoFormulario formulario) {
        
        try {
            // 1. Chama o novo serviço de domínio
            Contrato contratoAtualizado = contratoService.renovarContrato(
                contratoId,
                formulario.duracaoMeses(),
                formulario.salario(),
                formulario.status()
            );
            // 2. Retorna 200 OK com o contrato atualizado
            return ResponseEntity.ok(contratoAtualizado); 
        } catch (Exception e) {
            throw new RuntimeException("Erro ao renovar contrato: " + e.getMessage(), e);
        }
    }
    // --- (FIM DA NOVA FUNCIONALIDADE "RENOVAR") ---
}