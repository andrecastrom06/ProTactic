package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.Optional; 
import org.springframework.beans.factory.annotation.Autowired;
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

// --- (INÍCIO DA CORREÇÃO) ---
// Precisamos injetar o repositório 'LesaoRepository' (do domínio)
// e o 'JogadorRepository' para podermos salvar o plano customizado.
// Também precisamos do 'Lesao' (a entidade de domínio).
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;
// --- (FIM DA CORREÇÃO) ---


@RestController
@RequestMapping("backend/lesao") // Define a URL base
public class RegistroDeLesaoControlador {

    // --- Injeção dos Serviços ---
    private @Autowired LesaoServicoAplicacao lesaoServicoAplicacao;
    private @Autowired RegistroLesoesServico registroLesoesServico;

    // --- (INÍCIO DA CORREÇÃO) ---
    // Injetamos os repositórios necessários para o novo 'salvar-plano'
    private @Autowired LesaoRepository lesaoRepository;
    private @Autowired JogadorRepository jogadorRepository;
    // --- (FIM DA CORREÇÃO) ---
    

    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)
    @GetMapping(path = "pesquisa")
    public List<LesaoResumo> pesquisarResumos() {
        return lesaoServicoAplicacao.pesquisarResumos();
    }
    // ... (outros GETs omitidos para brevidade) ...
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

    @PostMapping(path = "/registrar/{atletaId}")
    public void registrarLesao(
            @PathVariable("atletaId") String atletaId, 
            @RequestBody RegistrarLesaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        // Este método já chama o 'RegistroLesoesRepositoryImpl'
        // que ignora os erros do BDD, está correto.
        registroLesoesServico.registrarLesao(atletaId, formulario.grau());
    }

    
    // --- (INÍCIO DA CORREÇÃO) ---

    /**
     * DTO/Formulário para receber os 'dias' (tempo) E o 'plano' (descrição) via JSON.
     * Exemplo de JSON: 
     * { 
     * "tempo": "Aproximadamente 3 semanas",
     * "plano": "Tratamento intensivo no departamento médico e fisioterapia."
     * }
     */
    public record PlanoRecuperacaoFormulario(
        String tempo, // Mudamos de 'int dias' para 'String tempo'
        String plano  // Adicionamos o 'plano'
    ) {}

    
    /**
     * Endpoint para POST /backend/lesao/cadastrar-plano/{atletaId}
     * (Corrigido para aceitar texto livre para TEMPO e PLANO)
     */
    @PostMapping(path = "/cadastrar-plano/{atletaId}")
    public void cadastrarPlanoRecuperacao(
            @PathVariable("atletaId") String atletaId, 
            @RequestBody PlanoRecuperacaoFormulario formulario) {

        if (formulario == null) {
            throw new IllegalArgumentException("O corpo da requisição (formulário) não pode ser nulo.");
        }
        
        // 1. O BDD ('RegistroLesoesServico') não suporta esta operação
        //    (ele só aceita 'int dias').
        // 2. Por isso, vamos "saltar" o serviço de domínio e usar
        //    diretamente os repositórios (como fizemos no BDD 'salvarPlanoDias').
        
        // 3. Buscar o Jogador (para obter o ID)
        Jogador jogador = jogadorRepository.buscarPorNome(atletaId);
        if (jogador == null) {
            throw new RuntimeException("Atleta não encontrado com o nome: " + atletaId);
        }

        // 4. Buscar a Lesão Ativa
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());
        if (lesaoOpt.isEmpty()) {
            throw new IllegalStateException("Nenhuma lesão ativa encontrada para salvar o plano de dias.");
        }
        
        Lesao lesao = lesaoOpt.get();

        // 5. Definir o TEMPO e o PLANO customizados
        lesao.setTempo(formulario.tempo());
        lesao.setPlano(formulario.plano());
        
        // 6. Salvar a lesão atualizada
        lesaoRepository.salvar(lesao);
    }
    // --- (FIM DA CORREÇÃO) ---
}