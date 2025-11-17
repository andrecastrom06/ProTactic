package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService; 

import java.time.LocalDate; 

@RestController
@RequestMapping("backend/jogador")
@CrossOrigin(origins = "http://localhost:3000") 
public class JogadorControlador {

    @Autowired
    private CadastroDeAtletaService cadastroDeAtletaService;


    public record CadastrarAtletaFormulario(
        String nomeCompleto,
        String posicao,
        int idade,
        LocalDate validadeDoContrato,
        String situacaoContratual, 
        Integer clubeId 
    ) {}

    @PostMapping("/cadastrar")
    public ResponseEntity<Jogador> cadastrarNovoAtleta(
            @RequestBody CadastrarAtletaFormulario formulario) {
        
        try {
            CadastroDeAtletaService.DadosNovoAtleta dados = 
                new CadastroDeAtletaService.DadosNovoAtleta(
                    formulario.nomeCompleto(),
                    formulario.posicao(),
                    formulario.idade(),
                    formulario.validadeDoContrato(),
                    formulario.situacaoContratual(),
                    formulario.clubeId()
                );
            
            Jogador novoJogador = cadastroDeAtletaService.cadastrarNovoAtleta(dados);

            return ResponseEntity.ok(novoJogador);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}