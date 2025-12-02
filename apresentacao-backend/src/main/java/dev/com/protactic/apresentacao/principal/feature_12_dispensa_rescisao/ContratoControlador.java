package dev.com.protactic.apresentacao.principal.feature_12_dispensa_rescisao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;
import dev.com.protactic.aplicacao.principal.contrato.ContratoServicoAplicacao;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.UsuarioRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico.ContratoService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.IDispensaService; 

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/contrato")
public class ContratoControlador { 

    @Autowired
    private ContratoServicoAplicacao contratoServicoAplicacao;
    
    // IMPORTANTE: Injetamos a INTERFACE, não a classe concreta.
    // Graças à classe @Configuration que criamos, isso aqui será o PROXY.
    @Autowired
    private IDispensaService dispensaService;
    
    @Autowired
    private ContratoService contratoService;

    // Precisamos buscar o jogador e o usuário para passar ao Proxy
    @Autowired
    private JogadorRepository jogadorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping(path = "pesquisa")
    public List<ContratoResumo> pesquisarResumos() {
        return contratoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-clube/{clubeId}")
    public List<ContratoResumo> pesquisarResumosPorClube(@PathVariable("clubeId") Integer clubeId) {
        return contratoServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }

    /**
     * Endpoint para dispensar um jogador.
     * AGORA USA O PROXY DE PROTEÇÃO.
     * Exigimos o ID do usuário que está fazendo a ação (simulando sessão).
     */
    @PostMapping(path = "/dispensar/{jogadorId}")
    public ResponseEntity<String> dispensarJogador(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestHeader(value = "usuarioId", required = true) Integer usuarioId) { 
            // ^^^ Simulando que o Frontend manda o ID do usuário logado no Header
        
        try {
            // 1. Busca os objetos necessários para passar ao Proxy do Domínio
            Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
            if (jogador == null) return ResponseEntity.notFound().build();

            Usuario usuarioSolicitante = usuarioRepository.buscarPorId(usuarioId);
            if (usuarioSolicitante == null) return ResponseEntity.badRequest().body("Usuário não encontrado");

           
            dispensaService.dispensarJogador(jogador, usuarioSolicitante);
            
            return ResponseEntity.ok("Jogador dispensado com sucesso.");

        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }

    public record RenovacaoFormulario(
        int duracaoMeses,
        double salario,
        String status
    ) {}

    @PutMapping(path = "/renovar/{contratoId}")
    public ResponseEntity<Contrato> renovarContrato(
            @PathVariable("contratoId") Integer contratoId,
            @RequestBody RenovacaoFormulario formulario) {
        
        try {
            Contrato contratoAtualizado = contratoService.renovarContrato(
                contratoId,
                formulario.duracaoMeses(),
                formulario.salario(),
                formulario.status()
            );
            
            return ResponseEntity.ok(contratoAtualizado); 
        } catch (Exception e) {
            throw new RuntimeException("Erro ao renovar contrato: " + e.getMessage(), e);
        }
    }
}