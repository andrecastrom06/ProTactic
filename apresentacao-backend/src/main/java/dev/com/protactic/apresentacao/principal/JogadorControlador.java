package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

import java.time.LocalDate; 

@RestController
@RequestMapping("backend/jogador")
@CrossOrigin(origins = "http://localhost:3000") 
public class JogadorControlador {

    @Autowired
    private JogadorRepository jogadorRepository;
    @Autowired
    private ContratoRepository contratoRepository;


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

            Contrato novoContrato = new Contrato();
            novoContrato.setClubeId(formulario.clubeId());
            novoContrato.setStatus(formulario.situacaoContratual());
            
            novoContrato.setDuracaoMeses(36); 
            novoContrato.setSalario(0); 
            
            contratoRepository.salvar(novoContrato); 

            
            Jogador novoJogador = new Jogador();
            novoJogador.setNome(formulario.nomeCompleto());
            novoJogador.setPosicao(formulario.posicao());
            novoJogador.setIdade(formulario.idade());
            
         
            novoJogador.setContratoId(novoContrato.getId());
            novoJogador.setClubeId(formulario.clubeId());
            

            novoJogador.setCompeticaoId(1);
            novoJogador.setContratoAtivo(true);
            novoJogador.setSaudavel(true);
            novoJogador.setGrauLesao(-1);
            novoJogador.setStatus("Disponível");
            novoJogador.setChegadaNoClube(LocalDate.now()); 

            jogadorRepository.salvar(novoJogador); 

            return ResponseEntity.ok(novoJogador);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}