package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;
import dev.com.protactic.aplicacao.principal.contrato.ContratoServicoAplicacao;

import dev.com.protactic.dominio.principal.Contrato; 
import dev.com.protactic.dominio.principal.dispensa.DispensaService;
import dev.com.protactic.dominio.principal.contrato.ContratoService; 

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/contrato")
public class ContratoControlador { 


    private @Autowired ContratoServicoAplicacao contratoServicoAplicacao;
    private @Autowired DispensaService dispensaService;
    
    private @Autowired ContratoService contratoService;
    
    @GetMapping(path = "pesquisa")
    public List<ContratoResumo> pesquisarResumos() {
        return contratoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-clube/{clubeId}")
    public List<ContratoResumo> pesquisarResumosPorClube(@PathVariable("clubeId") Integer clubeId) {
        return contratoServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }

    
  
    @PostMapping(path = "/dispensar/{jogadorId}")
    public void dispensarJogador(@PathVariable("jogadorId") Integer jogadorId) {
        
        try {
            dispensaService.dispensarJogadorPorId(jogadorId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar dispensar o jogador: " + e.getMessage(), e);
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