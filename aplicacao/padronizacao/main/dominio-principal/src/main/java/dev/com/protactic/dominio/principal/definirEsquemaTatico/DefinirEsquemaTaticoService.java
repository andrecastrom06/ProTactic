package dev.com.protactic.dominio.principal.definirEsquemaTatico;

import java.util.List;
import java.util.Objects;
import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;

public class DefinirEsquemaTaticoService {

    private final EscalacaoRepository repository;
    private final JogadorRepository jogadorRepository;
    private final RegistroLesoesRepository registroLesoesRepository;
    private final RegistroCartoesService registroCartoesService;

    public DefinirEsquemaTaticoService(EscalacaoRepository repository,
                                       JogadorRepository jogadorRepository,
                                       RegistroLesoesRepository registroLesoesRepository,
                                       RegistroCartoesService registroCartoesService) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
        this.registroLesoesRepository = registroLesoesRepository;
        this.registroCartoesService = registroCartoesService;
    }
    
    public boolean registrarJogadorEmEscalacao(String jogoData, String nomeJogador, int clubeId) throws Exception {
        Objects.requireNonNull(jogoData, "A data do jogo não pode ser nula.");
        Objects.requireNonNull(nomeJogador, "O nome do jogador não pode ser nulo.");

        if (jogadorRepository.buscarPorNome(nomeJogador) == null) {
            throw new Exception("Jogador não encontrado: " + nomeJogador);
        }

        boolean contratoAtivo = registroLesoesRepository.contratoAtivo(nomeJogador);
        Suspensao suspensaoObj = registroCartoesService.verificarSuspensao(nomeJogador);
        boolean suspenso = (suspensaoObj != null) ? suspensaoObj.isSuspenso() : false;
        
        int grauLesao = registroLesoesRepository.grauLesaoAtiva(nomeJogador).orElse(0);

        return this.registrarEscalacao(jogoData, nomeJogador, contratoAtivo, suspenso, grauLesao, clubeId);
    }

    private boolean registrarEscalacao(String jogoData, String nomeJogador,
                                      boolean contratoAtivo, boolean suspenso, int grauLesao, int clubeId) {

        boolean valido = contratoAtivo && !suspenso && (grauLesao <= 0);

        if (valido) {
            repository.salvarJogadorNaEscalacao(jogoData, nomeJogador, clubeId);
            return true;
        }
        return false;
    }

    public List<String> obterEscalacao(String jogoData, int clubeId) {
        return repository.obterEscalacaoPorData(jogoData, clubeId);
    }
}