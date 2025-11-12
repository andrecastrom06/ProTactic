package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // <-- Importa o ResponseEntity
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;
import dev.com.protactic.aplicacao.principal.lesao.LesaoServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos)
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesServico;

// Importa Entidades e Repositórios necessários
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;


@RestController
@RequestMapping("backend/lesao") 
public class RegistroDeLesaoControlador { // <-- Nome do ficheiro atualizado

    // --- Injeção dos Serviços ---
    private @Autowired LesaoServicoAplicacao lesaoServicoAplicacao;
    private @Autowired RegistroLesoesServico registroLesoesServico;
    private @Autowired LesaoRepository lesaoRepository;
    private @Autowired JogadorRepository jogadorRepository;
    
    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)
    @GetMapping(path = "pesquisa")
    public List<LesaoResumo> pesquisarResumos() {
        return lesaoServicoAplicacao.pesquisarResumos();
    }
    
    @GetMapping(path = "pesquisa-por-jogador/{jogadorId}")
    public List<LesaoResumo> pesquisarResumosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return lesaoServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }
    
    @GetMapping(path = "pesquisa-ativa-por-jogador/{jogadorId}")
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return lesaoServicoAplicacao.pesquisarResumoAtivoPorJogador(jogadorId);
    }


    // --- Endpoints de COMANDO (POST) ---

    public record RegistrarLesaoFormulario(
        int grau
    ) {}

    // --- (INÍCIO DA CORREÇÃO 1) ---
    // Alterado de "/registrar/{atletaId}" (String) para "/registrar/{jogadorId}" (Integer)
    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<Void> registrarLesao(
            @PathVariable("jogadorId") Integer jogadorId, // <-- Recebe como Integer
            @RequestBody RegistrarLesaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        // 1. "Traduzir" o ID para o NOME
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 2. Chamar o serviço com o NOME (como o BDD espera)
            registroLesoesServico.registrarLesao(jogador.getNome(), formulario.grau());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Se a regra de negócio falhar (ex: já lesionado), lança a exceção
            throw new RuntimeException("Erro ao registrar lesão para " + jogador.getNome() + ": " + e.getMessage(), e);
        }
    }
    // --- (FIM DA CORREÇÃO 1) ---

    
    public record PlanoRecuperacaoFormulario(
        String tempo, 
        String plano
    ) {}

    
    // --- (INÍCIO DA CORREÇÃO 2) ---
    // Alterado de "/cadastrar-plano/{atletaId}" (String) para "/cadastrar-plano/{jogadorId}" (Integer)
    @PostMapping(path = "/cadastrar-plano/{jogadorId}")
    public ResponseEntity<Void> cadastrarPlanoRecuperacao(
            @PathVariable("jogadorId") Integer jogadorId, // <-- Recebe como Integer
            @RequestBody PlanoRecuperacaoFormulario formulario) {

        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        // 1. Buscar o Jogador
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. Buscar a Lesão Ativa (Esta parte já estava correta, usando o ID)
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());
        if (lesaoOpt.isEmpty()) {
            // Não pode adicionar plano se não houver lesão
            return ResponseEntity.status(404).body(null); // 404 - Lesão não encontrada
        }
        
        Lesao lesao = lesaoOpt.get();

        // 3. Definir o TEMPO e o PLANO customizados
        lesao.setTempo(formulario.tempo());
        lesao.setPlano(formulario.plano());
        
        // 4. Salvar a lesão atualizada
        lesaoRepository.salvar(lesao);
        return ResponseEntity.ok().build();
    }
    // --- (FIM DA CORREÇÃO 2) ---


    // --- (INÍCIO DA CORREÇÃO 3) ---
    // Alterado de "/encerrar/{atletaId}" (String) para "/encerrar/{jogadorId}" (Integer)
    @PostMapping(path = "/encerrar/{jogadorId}")
    public ResponseEntity<Void> encerrarLesao(@PathVariable("jogadorId") Integer jogadorId) { // <-- Recebe como Integer
        
        // 1. "Traduzir" o ID para o NOME
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }
        
        try {
            // 2. Chamar o serviço com o NOME (como o BDD espera)
            registroLesoesServico.encerrarRecuperacao(jogador.getNome());
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar encerrar lesão para ID " + jogadorId + ": " + e.getMessage(), e);
        }
    }
    // --- (FIM DA CORREÇÃO 3) ---
}