package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.List;
import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.Suspensao;

/**
 * Versão ATUALIZADA e LIMPA.
 * - Removemos o construtor antigo (de 1 argumento).
 * - Agora só existe o construtor de 2 argumentos.
 * - Os repositórios agora podem ser 'final'.
 * - Removemos os 'if (suspensaoRepository != null)' pois ele sempre existirá.
 */
public class RegistroCartoesService {

    private final RegistroCartoesRepository repository;
    private final SuspensaoRepository suspensaoRepository; // Agora é 'final'

    /**
     * Único construtor.
     * Força que o serviço SEMPRE tenha os dois repositórios.
     */
    public RegistroCartoesService(RegistroCartoesRepository cartaoRepository, SuspensaoRepository suspensaoRepository) {
        this.repository = cartaoRepository;
        this.suspensaoRepository = suspensaoRepository;
    }

    public void registrarCartao(String atleta, String tipo) {
        // 1. Salva o evento
        repository.salvarCartao(new RegistroCartao(atleta, tipo));

        // 2. Calcula o novo estado
        Suspensao estadoAtual = verificarSuspensao(atleta);

        // 3. Persiste o novo estado (sem 'if')
        this.suspensaoRepository.salvarOuAtualizar(estadoAtual);
    }

    public Suspensao verificarSuspensao(String atleta) {
        // Lógica de cálculo (não muda)
        List<RegistroCartao> cartoes = repository.buscarCartoesPorAtleta(atleta);
        long amarelos = cartoes.stream().filter(c -> "amarelo".equals(c.getTipo())).count();
        long vermelhos = cartoes.stream().filter(c -> "vermelho".equals(c.getTipo())).count();
        boolean suspenso = amarelos >= 3 || vermelhos >= 1;
        return new Suspensao(0, atleta, suspenso, (int) amarelos, (int) vermelhos);
    }

    public void limparCartoes(String atleta) {
        // 1. Limpa o log
        repository.limparCartoes(atleta);

        // 2. Recalcula o estado
        Suspensao estadoLimpo = verificarSuspensao(atleta);

        // 3. Persiste o estado limpo (sem 'if')
        this.suspensaoRepository.salvarOuAtualizar(estadoLimpo);
    }
}