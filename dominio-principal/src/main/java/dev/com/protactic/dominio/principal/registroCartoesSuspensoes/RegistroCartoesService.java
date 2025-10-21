package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.List;

import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.Suspensao;

public class RegistroCartoesService {

    private final RegistroCartoesRepository repository;

    public RegistroCartoesService(RegistroCartoesRepository repository) {
        this.repository = repository;
    }

    public void registrarCartao(String atleta, String tipo) {
        repository.salvarCartao(new RegistroCartao(atleta, tipo));
    }

    public Suspensao verificarSuspensao(String atleta) {
        List<RegistroCartao> cartoes = repository.buscarCartoesPorAtleta(atleta);

        long amarelos = cartoes.stream().filter(c -> "amarelo".equals(c.getTipo())).count();
        long vermelhos = cartoes.stream().filter(c -> "vermelho".equals(c.getTipo())).count();

        boolean suspenso = amarelos >= 3 || vermelhos >= 1;

        return new Suspensao(0, atleta, suspenso, (int) amarelos, (int) vermelhos);
    }

    public void limparCartoes(String atleta) {
        repository.limparCartoes(atleta);
    }
}
