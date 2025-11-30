package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.List;
import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.Suspensao;

public class RegistroCartoesService {

    private final RegistroCartoesRepository repository;
    private final SuspensaoRepository suspensaoRepository;

    public RegistroCartoesService(RegistroCartoesRepository cartaoRepository, SuspensaoRepository suspensaoRepository) {
        this.repository = cartaoRepository;
        this.suspensaoRepository = suspensaoRepository;
    }

    public void registrarCartao(String atleta, String tipo) {
        repository.salvarCartao(new RegistroCartao(atleta, tipo));
        Suspensao estadoAtual = verificarSuspensao(atleta);
        this.suspensaoRepository.salvarOuAtualizar(estadoAtual);
    }

    public Suspensao verificarSuspensao(String atleta) {
        List<RegistroCartao> cartoes = repository.buscarCartoesPorAtleta(atleta);
        long amarelos = cartoes.stream().filter(c -> "amarelo".equalsIgnoreCase(c.getTipo())).count();
        long vermelhos = cartoes.stream().filter(c -> "vermelho".equalsIgnoreCase(c.getTipo())).count();
        boolean suspenso = amarelos >= 3 || vermelhos >= 1;
        return new Suspensao(0, atleta, suspenso, (int) amarelos, (int) vermelhos);
    }

    public void limparCartoes(String atleta) {
        repository.limparCartoes(atleta);
        Suspensao estadoLimpo = verificarSuspensao(atleta);
        this.suspensaoRepository.salvarOuAtualizar(estadoLimpo);
    }

    public List<Suspensao> listarSuspensosPorClube(Integer clubeId) {
        return suspensaoRepository.buscarSuspensosPorClube(clubeId);
    }
}