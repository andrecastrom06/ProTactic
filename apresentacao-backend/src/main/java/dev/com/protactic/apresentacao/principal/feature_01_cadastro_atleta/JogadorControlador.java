package dev.com.protactic.apresentacao.principal.feature_01_cadastro_atleta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService; 


@RestController
@RequestMapping("backend/jogador")
@CrossOrigin(origins = "http://localhost:3000") 
public class JogadorControlador {

    @Autowired
    private CadastroDeAtletaService cadastroDeAtletaService;

    // --- CORREÇÃO AQUI: Adicionados pernaDominante e minutagem ---
    public record CadastrarAtletaFormulario(
        String nomeCompleto,
        String posicao,
        int idade,
        String pernaDominante, // <--- Novo campo
        int minutagem,         // <--- Novo campo
        int duracaoMeses, 
        Double salario,
        String situacaoContratual, 
        Integer clubeId 
    ) {}

    @PostMapping("/cadastrar")
    public ResponseEntity<Jogador> cadastrarNovoAtleta(
            @RequestBody CadastrarAtletaFormulario formulario) {
        
        try {
            double salario = formulario.salario() != null ? formulario.salario() : 0.0;

            CadastroDeAtletaService.DadosNovoAtleta dados = 
                new CadastroDeAtletaService.DadosNovoAtleta(
                    formulario.nomeCompleto(),
                    formulario.posicao(),
                    formulario.idade(),
                    formulario.pernaDominante(), 
                    formulario.minutagem(),      
                    formulario.duracaoMeses(),    
                    salario,
                    formulario.situacaoContratual(),
                    formulario.clubeId()
                );
            
            Jogador novoJogador = cadastroDeAtletaService.cadastrarNovoAtleta(dados);

            return ResponseEntity.ok(novoJogador);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/listar/{clubeId}")
    public ResponseEntity<List<Jogador>> listarPorClube(@PathVariable Integer clubeId) {
        try {
            List<Jogador> jogadores = cadastroDeAtletaService.listarJogadoresDoClube(clubeId);
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}