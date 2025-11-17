package dev.com.protactic.dominio.principal.planejamentoCargaSemanal;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.TreinoSemanal;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import java.util.Objects;

public class PlanejamentoCargaSemanalService {

    private final PlanejamentoCargaSemanalRepositoryMock repository;
    private final JogadorRepository jogadorRepository; 

    public PlanejamentoCargaSemanalService(PlanejamentoCargaSemanalRepositoryMock repository,
                                           JogadorRepository jogadorRepository) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
    }

    public boolean registrarTreinoPorId(Integer jogadorId) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador com ID " + jogadorId + " não encontrado.");
        }
        
        return this.registrarTreino(jogador);
    }

    public boolean registrarTreino(Jogador jogador) {
        boolean podeRegistrar = jogador.isContratoAtivo() &&
                                (jogador.getGrauLesao() == -1 || jogador.getGrauLesao() == 0);

        TreinoSemanal treino = new TreinoSemanal(jogador);
        treino.setRegistrado(podeRegistrar);

        repository.salvarTreino(treino);

        return podeRegistrar;
    }
}