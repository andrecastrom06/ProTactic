package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

import java.time.LocalDate; // Para a data de contrato

/**
 * Controlador de API REST para gerir a entidade Jogador
 * (Criar, Ler, Atualizar, Deletar)
 */
@RestController
@RequestMapping("backend/jogador")
@CrossOrigin(origins = "http://localhost:3000") // Permite acesso do React
public class JogadorControlador {

    @Autowired
    private JogadorRepository jogadorRepository;
    @Autowired
    private ContratoRepository contratoRepository;

    /**
     * DTO (Formulário) para o popup "Cadastrar Novo Atleta"
     * (Baseado em image_988ce3.png)
     */
    public record CadastrarAtletaFormulario(
        String nomeCompleto,
        String posicao,
        int idade,
        LocalDate validadeDoContrato,
        String situacaoContratual, // Ex: "Com contrato ativo"
        Integer clubeId // O ID do clube do usuário logado
    ) {}

    /**
     * Endpoint para POST /backend/jogador/cadastrar
     * Implementa a funcionalidade do modal "Novo Atleta"
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<Jogador> cadastrarNovoAtleta(
            @RequestBody CadastrarAtletaFormulario formulario) {
        
        try {
            // 1. Criar o Contrato primeiro
            Contrato novoContrato = new Contrato();
            novoContrato.setClubeId(formulario.clubeId());
            novoContrato.setStatus(formulario.situacaoContratual());
            
            novoContrato.setDuracaoMeses(36); // Mock por agora
            novoContrato.setSalario(0); // O popup não pede salário
            
            contratoRepository.salvar(novoContrato); // Salva e obtém o ID

            // 2. Criar o novo Jogador
            Jogador novoJogador = new Jogador();
            novoJogador.setNome(formulario.nomeCompleto());
            novoJogador.setPosicao(formulario.posicao());
            novoJogador.setIdade(formulario.idade());
            
            // 3. Ligar o Jogador ao Contrato e Clube
            novoJogador.setContratoId(novoContrato.getId());
            novoJogador.setClubeId(formulario.clubeId());
            
            // 4. Preencher dados "default" (baseado no V2__Populate)
            novoJogador.setCompeticaoId(1);
            novoJogador.setContratoAtivo(true);
            novoJogador.setSaudavel(true);
            novoJogador.setGrauLesao(-1);
            novoJogador.setStatus("Disponível");
            novoJogador.setChegadaNoClube(LocalDate.now()); // Data de hoje

            jogadorRepository.salvar(novoJogador); // Salva o jogador

            return ResponseEntity.ok(novoJogador);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}