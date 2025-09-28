package dev.com.protactic.dominio.principal.planejamentoCargaSemanal;

import dev.com.protactic.dominio.principal.Jogador;

public class PlanejamentoCargaSemanalService {

    private final PlanejamentoCargaSemanalRepositoryMock repository;

    public PlanejamentoCargaSemanalService(PlanejamentoCargaSemanalRepositoryMock repository) {
        this.repository = repository;
    }

    /**
     * Regras de negócio:
     * - Jogador precisa ter contrato ativo
     * - Jogador precisa estar saudável (-1) ou com desconforto (0)
     */
    public boolean registrarTreino(Jogador jogador) {
        boolean podeRegistrar = jogador.isContratoAtivo() &&
                                (jogador.getGrauLesao() == -1 || jogador.getGrauLesao() == 0);

        TreinoSemanal treino = new TreinoSemanal(jogador);
        treino.setRegistrado(podeRegistrar);

        repository.salvarTreino(treino);

        return podeRegistrar;
    }
}
