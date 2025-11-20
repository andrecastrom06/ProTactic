package dev.com.protactic.aplicacao.principal.formacao;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FormacaoServicoAplicacao {

    private final FormacaoRepositorioAplicacao formacaoRepository; 
    private final JogadorRepository jogadorRepository;
    private final RegistroLesoesRepository registroLesoesRepository;
    private final RegistroCartoesService registroCartoesService;

    public FormacaoServicoAplicacao(
            FormacaoRepositorioAplicacao formacaoRepository,
            JogadorRepository jogadorRepository,
            RegistroLesoesRepository registroLesoesRepository,
            RegistroCartoesService registroCartoesService) {
        this.formacaoRepository = formacaoRepository;
        this.jogadorRepository = jogadorRepository;
        this.registroLesoesRepository = registroLesoesRepository;
        this.registroCartoesService = registroCartoesService;
    }

    // CORREÇÃO AQUI: Adicionado clubeId
    public record FormacaoDados(
        Integer partidaId,
        String esquema,
        List<Integer> jogadoresIds,
        Integer clubeId 
    ) {}

    public FormacaoResumo salvarFormacao(FormacaoDados formulario) throws Exception {
        List<String> erros = validarAptidaoJogadores(formulario.jogadoresIds(), formulario.partidaId());
        if (!erros.isEmpty()) {
            throw new Exception("A escalação não pôde ser salva: " + String.join(", ", erros));
        }
        
        // Repassa o clubeId para o repositório
        return formacaoRepository.salvar(
            formulario.partidaId(),
            formulario.esquema(),
            formulario.jogadoresIds(),
            formulario.clubeId()
        );
    }

    public FormacaoResumo editarFormacao(Integer formacaoId, FormacaoDados formulario) throws Exception {
        List<String> erros = validarAptidaoJogadores(formulario.jogadoresIds(), formulario.partidaId());
        if (!erros.isEmpty()) {
            throw new Exception("A escalação não pôde ser editada: " + String.join(", ", erros));
        }
        
        return formacaoRepository.editar(
            formacaoId,
            formulario.partidaId(),
            formulario.esquema(),
            formulario.jogadoresIds(),
            formulario.clubeId() 
        );
    }
    
    public Optional<FormacaoResumo> buscarPorPartida(Integer partidaId) {
        return formacaoRepository.buscarResumoPorPartidaId(partidaId);
    }

    private List<String> validarAptidaoJogadores(List<Integer> jogadoresIds, Integer partidaId) {
        // ... (seu código de validação permanece igual) ...
        List<String> jogadoresInaptos = new ArrayList<>();
        if (jogadoresIds == null || jogadoresIds.size() != 11) {
             jogadoresInaptos.add("A escalação deve ter exatamente 11 jogadores.");
             return jogadoresInaptos;
        }

        for (Integer jogadorId : jogadoresIds) {
            Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
            if (jogador == null) {
                jogadoresInaptos.add("Jogador com ID " + jogadorId + " não encontrado.");
                continue; 
            }
            
            String atletaNome = jogador.getNome();
            
            boolean contratoAtivo = registroLesoesRepository.contratoAtivo(atletaNome);
            Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(atletaNome);
            boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;
            int grauLesao = registroLesoesRepository.grauLesaoAtiva(atletaNome).orElse(0);

            try {
                boolean apto = (contratoAtivo && !suspenso && (grauLesao <= 0));
                
                if (!apto) {
                    jogadoresInaptos.add(atletaNome + " (Contrato: " + contratoAtivo + ", Suspenso: " + suspenso + ", Lesão: " + grauLesao + ")");
                }
            } catch (Exception e) {
                jogadoresInaptos.add("Erro ao verificar jogador " + atletaNome + ": " + e.getMessage());
            }
        }
        return jogadoresInaptos;
    }
}