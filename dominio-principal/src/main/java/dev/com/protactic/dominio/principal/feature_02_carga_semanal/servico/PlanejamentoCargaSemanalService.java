package dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculadoraCargaStrategy;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaLinear;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.TreinoSemanal;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.PlanejamentoCargaSemanalRepositoryMock;

import java.math.BigDecimal;
import java.util.Objects;

public class PlanejamentoCargaSemanalService {

    private final PlanejamentoCargaSemanalRepositoryMock repository;
    private final JogadorRepository jogadorRepository;

    public PlanejamentoCargaSemanalService(PlanejamentoCargaSemanalRepositoryMock repository,
                                           JogadorRepository jogadorRepository) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
    }

    public boolean registrarTreinoPorId(Integer jogadorId, double duracao, double intensidade, CalculadoraCargaStrategy estrategia) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador com ID " + jogadorId + " não encontrado.");
        }
        return this.registrarTreino(jogador, duracao, intensidade, estrategia);
    }

    public boolean registrarTreino(Jogador jogador, double duracao, double intensidade, CalculadoraCargaStrategy estrategia) {
        boolean podeRegistrar = jogador.isContratoAtivo() &&
                                (jogador.getGrauLesao() == -1 || jogador.getGrauLesao() == 0);

        if (!podeRegistrar) {
            System.out.println("Bloqueado: Jogador lesionado ou sem contrato.");
            return false; 
        }

        if (estrategia == null) {
            estrategia = new CalculoCargaLinear();
        }

        TreinoSemanal treino = new TreinoSemanal(jogador);
        treino.setDuracaoMinutos(duracao);
        treino.setIntensidade(intensidade);

        BigDecimal cargaCalculada = estrategia.calcularCarga(duracao, intensidade);
        treino.setCargaTotal(cargaCalculada);
        
        treino.setRegistrado(true);

        repository.salvarTreino(treino);

        System.out.println("Treino registrado com sucesso para " + jogador.getNome() + 
                           ". Carga: " + cargaCalculada);

        return true;
    }
}