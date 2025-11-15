package dev.com.protactic.apresentacao.principal;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional; // <-- 1. Importação necessária
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // <-- 2. Importação necessária
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // <-- 3. Importação necessária
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable; // <-- 4. Importação necessária
import org.springframework.web.bind.annotation.RestController;

// Importa a entidade JPA e o repositório
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoRepositorySpringData;

// Importa os serviços/repositórios para a verificação de aptidão
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.DefinirEsquemaTaticoService;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;


@RestController
@RequestMapping("backend/formacao")
@CrossOrigin(origins = "http://localhost:3000")

public class FormacaoControlador {

    // --- Injeção dos Repositórios e Serviços ---
    
    private @Autowired EscalacaoRepositorySpringData escalacaoRepository;
    
    // Serviços e Repositórios para a lógica de aptidão (BDD #4)
    private @Autowired DefinirEsquemaTaticoService definirEsquemaTaticoService;
    private @Autowired JogadorRepository jogadorRepository;
    private @Autowired RegistroLesoesRepository registroLesoesRepository;
    private @Autowired RegistroCartoesService registroCartoesService;
    

    /**
     * DTO/Formulário (reutilizado para Criar e Editar)
     */
    public record FormacaoFormulario(
        Integer partidaId,
        String esquema,
        List<Integer> jogadoresIds
    ) {}

    private List<String> validarAptidaoJogadores(List<Integer> jogadoresIds, Integer partidaId) {
        
        List<String> jogadoresInaptos = new ArrayList<>();
        if (jogadoresIds == null || jogadoresIds.size() != 11) {
             jogadoresInaptos.add("A escalação deve ter exatamente 11 jogadores.");
             return jogadoresInaptos;
        }

        String jogoData = "partida-" + partidaId; 

        for (Integer jogadorId : jogadoresIds) {
            
            Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
            if (jogador == null) {
                jogadoresInaptos.add("Jogador com ID " + jogadorId + " não encontrado.");
                continue; // Pula para o próximo jogador
            }
            
            String atletaNome = jogador.getNome();
            
            boolean contratoAtivo = registroLesoesRepository.contratoAtivo(atletaNome);
            Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(atletaNome);
            boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;
            int grauLesao = registroLesoesRepository.grauLesaoAtiva(atletaNome).orElse(0);

            try {
                // Chamamos o Serviço de Domínio (BDD #4)
                boolean apto = definirEsquemaTaticoService.registrarEscalacao(
                    jogoData, atletaNome, contratoAtivo, suspenso, grauLesao
                );
                
                if (!apto) {
                    jogadoresInaptos.add(atletaNome + " (Contrato: " + contratoAtivo + ", Suspenso: " + suspenso + ", Lesão: " + grauLesao + ")");
                }

            } catch (Exception e) {
                jogadoresInaptos.add("Erro ao verificar jogador " + atletaNome + ": " + e.getMessage());
            }
        }
        
        return jogadoresInaptos;
    }
    // --- (FIM DO NOVO MÉTODO PRIVADO DE VALIDAÇÃO) ---


    /**
     * Endpoint para POST /backend/formacao/salvar
     * (CRIAR nova formação)
     */
    @PostMapping(path = "/salvar")
    public ResponseEntity<?> salvarFormacao(@RequestBody FormacaoFormulario formulario) {
        
        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }
        
        // 1. CHAMA O MÉTODO DE VALIDAÇÃO REUTILIZADO
        List<String> erros = validarAptidaoJogadores(formulario.jogadoresIds(), formulario.partidaId());

        // 2. Verificação final: Se a lista de inaptos não estiver vazia, falhamos!
        if (!erros.isEmpty()) {
            // Retorna 400 Bad Request com a lista de erros
            return ResponseEntity.badRequest().body("A escalação não pôde ser salva: " + String.join(", ", erros));
        }
        
        // 3. SUCESSO! Salva a nova escalação.
        EscalacaoJPA novaEscalacao = new EscalacaoJPA();
        novaEscalacao.setPartidaId(formulario.partidaId());
        novaEscalacao.setEsquema(formulario.esquema());
        
        // (Método auxiliar para preencher os 11 jogadores)
        preencherJogadores(novaEscalacao, formulario.jogadoresIds());
        
        EscalacaoJPA escalacaoSalva = escalacaoRepository.save(novaEscalacao);
        return ResponseEntity.ok(escalacaoSalva); // Retorna 200 OK com o objeto criado
    }

    @PutMapping(path = "/editar/{formacaoId}")
    public ResponseEntity<?> editarFormacao(
            @PathVariable("formacaoId") Integer formacaoId,
            @RequestBody FormacaoFormulario formulario) {

        if (formulario == null) {
            return ResponseEntity.badRequest().body("Formulário não pode ser nulo.");
        }

        // 1. Busca a formação existente no banco
        Optional<EscalacaoJPA> formacaoOpt = escalacaoRepository.findById(formacaoId);
        if (formacaoOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }

        // 2. CHAMA O MESMO MÉTODO DE VALIDAÇÃO
        List<String> erros = validarAptidaoJogadores(formulario.jogadoresIds(), formulario.partidaId());

        if (!erros.isEmpty()) {
            // Retorna 400 Bad Request com a lista de erros
            return ResponseEntity.badRequest().body("A escalação não pôde ser editada: " + String.join(", ", erros));
        }

        // 3. SUCESSO! Atualiza a formação existente.
        EscalacaoJPA formacaoExistente = formacaoOpt.get();
        formacaoExistente.setPartidaId(formulario.partidaId());
        formacaoExistente.setEsquema(formulario.esquema());

        // (Método auxiliar para preencher os 11 jogadores)
        preencherJogadores(formacaoExistente, formulario.jogadoresIds());

        EscalacaoJPA escalacaoAtualizada = escalacaoRepository.save(formacaoExistente);
        return ResponseEntity.ok(escalacaoAtualizada); // Retorna 200 OK com o objeto atualizado
    }
    
    private void preencherJogadores(EscalacaoJPA escalacao, List<Integer> jogadoresIds) {
        escalacao.setIdJogador1(jogadoresIds.get(0));
        escalacao.setIdJogador2(jogadoresIds.get(1));
        escalacao.setIdJogador3(jogadoresIds.get(2));
        escalacao.setIdJogador4(jogadoresIds.get(3));
        escalacao.setIdJogador5(jogadoresIds.get(4));
        escalacao.setIdJogador6(jogadoresIds.get(5));
        escalacao.setIdJogador7(jogadoresIds.get(6));
        escalacao.setIdJogador8(jogadoresIds.get(7));
        escalacao.setIdJogador9(jogadoresIds.get(8));
        escalacao.setIdJogador10(jogadoresIds.get(9));
        escalacao.setIdJogador11(jogadoresIds.get(10));
    }
}