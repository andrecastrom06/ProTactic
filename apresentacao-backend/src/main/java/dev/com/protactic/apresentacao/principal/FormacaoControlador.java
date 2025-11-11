package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.ArrayList; // Importar o ArrayList
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Importar ResponseEntity

// Importa a nova infraestrutura que acabámos de criar
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoRepositorySpringData;

// --- (INÍCIO DAS CORREÇÕES) ---
// Precisamos de todos os serviços/repositórios para a verificação de aptidão
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
// --- (FIM DAS CORREÇÕES) ---


/**
 * Controlador de API REST para a funcionalidade de Formação Tática
 * (Salva na tabela 'ESCALACAO' com 11 jogadores).
 */
@RestController
@RequestMapping("backend/formacao")
public class FormacaoControlador {

    // --- Injeção dos Repositórios e Serviços ---
    
    // Repositório para a tabela ESCALACAO (os 11 jogadores)
    private @Autowired EscalacaoRepositorySpringData escalacaoRepository;
    
    // Serviços e Repositórios para a lógica de aptidão (do BDD)
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService;
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired RegistroLesoesRepository registroLesoesRepository;
    private @Autowired RegistroCartoesService registroCartoesService;
    

    /**
     * DTO/Formulário para receber a escalação completa (11 jogadores).
     * Exemplo de JSON:
     * {
     * "partidaId": 1,
     * "esquema": "4-3-3",
     * "jogadoresIds": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
     * }
     */
    public record FormacaoFormulario(
        Integer partidaId,
        String esquema,
        List<Integer> jogadoresIds
    ) {}
    
    /**
     * Endpoint para POST /backend/formacao/salvar
     * (Agora verifica a aptidão ANTES de salvar)
     */
    @PostMapping(path = "/salvar")
    public void salvarFormacao(@RequestBody FormacaoFormulario formulario) {
        
        if (formulario == null) {
            throw new IllegalArgumentException("Formulário não pode ser nulo.");
        }
        
        // 1. Validar se a lista tem 11 jogadores
        if (formulario.jogadoresIds() == null || formulario.jogadoresIds().size() != 11) {
            throw new IllegalArgumentException("A escalação deve ter exatamente 11 jogadores.");
        }
        
        // --- (INÍCIO DA NOVA LÓGICA DE VERIFICAÇÃO) ---
        
        // 2. Verificar a aptidão de CADA jogador
        List<String> jogadoresInaptos = new ArrayList<>();
        // O BDD usa 'jogoData' (String), então vamos criar uma (ex: "partida-1")
        String jogoData = "partida-" + formulario.partidaId(); 

        for (Integer jogadorId : formulario.jogadoresIds()) {
            
            Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
            if (jogador == null) {
                throw new RuntimeException("Jogador com ID " + jogadorId + " não encontrado.");
            }
            
            String atletaNome = jogador.getNome();
            
            // 3. Buscamos os 3 argumentos da regra de negócio
            boolean contratoAtivo = registroLesoesRepository.contratoAtivo(atletaNome);
            Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(atletaNome);
            boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;
            int grauLesao = registroLesoesRepository.grauLesaoAtiva(atletaNome).orElse(0);

            try {
                // 4. Chamamos o seu Serviço de Domínio (que salva na ESCALACAOSIMPLES)
                boolean apto = definirEsquemaTaticoService.registrarEscalacao(
                    jogoData,
                    atletaNome,
                    contratoAtivo,
                    suspenso,
                    grauLesao
                );
                
                // 5. Se não estiver apto, adicionamos à lista de erro
                if (!apto) {
                    jogadoresInaptos.add(atletaNome + " (Contrato: " + contratoAtivo + ", Suspenso: " + suspenso + ", Lesão: " + grauLesao + ")");
                }

            } catch (Exception e) {
                // Se o próprio serviço falhar
                throw new RuntimeException("Erro ao verificar jogador " + atletaNome + ": " + e.getMessage(), e);
            }
        }

        // 6. Verificação final: Se a lista de inaptos não estiver vazia, falhamos!
        if (!jogadoresInaptos.isEmpty()) {
            throw new RuntimeException("A escalação não pôde ser salva. Os seguintes jogadores estão inaptos: " + String.join(", ", jogadoresInaptos));
        }
        
        // --- (FIM DA NOVA LÓGICA DE VERIFICAÇÃO) ---

        // 7. SUCESSO! Todos os 11 jogadores estão aptos.
        //    Agora, salvamos a escalação completa na tabela 'ESCALACAO'.
        
        EscalacaoJPA novaEscalacao = new EscalacaoJPA();
        novaEscalacao.setPartidaId(formulario.partidaId());
        novaEscalacao.setEsquema(formulario.esquema());
        
        novaEscalacao.setIdJogador1(formulario.jogadoresIds().get(0));
        novaEscalacao.setIdJogador2(formulario.jogadoresIds().get(1));
        novaEscalacao.setIdJogador3(formulario.jogadoresIds().get(2));
        novaEscalacao.setIdJogador4(formulario.jogadoresIds().get(3));
        novaEscalacao.setIdJogador5(formulario.jogadoresIds().get(4));
        novaEscalacao.setIdJogador6(formulario.jogadoresIds().get(5));
        novaEscalacao.setIdJogador7(formulario.jogadoresIds().get(6));
        novaEscalacao.setIdJogador8(formulario.jogadoresIds().get(7));
        novaEscalacao.setIdJogador9(formulario.jogadoresIds().get(8));
        novaEscalacao.setIdJogador10(formulario.jogadoresIds().get(9));
        novaEscalacao.setIdJogador11(formulario.jogadoresIds().get(10));
        
        escalacaoRepository.save(novaEscalacao);
    }
}