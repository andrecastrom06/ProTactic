package dev.com.protactic.dominio.principal.feature_07_definir_capitao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.entidade.Capitao;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.repositorio.CapitaoRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class CapitaoService {

    private final CapitaoRepository repository;
    private final JogadorRepository jogadorRepository;
    private final Clock clock;

    public CapitaoService(CapitaoRepository repository, JogadorRepository jogadorRepository, Clock clock) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
        this.clock = clock;
    }

    public Capitao buscarCapitaoPorClube(Integer clubeId) {
        Objects.requireNonNull(clubeId, "O ID do Clube não pode ser nulo.");
        return repository.buscarCapitaoPorClube(clubeId);
    }

    public void definirCapitaoPorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId); // Verifique se esse método existe no seu repo
        
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não encontrado com ID: " + jogadorId);
        }

        tentarDefinirCapitao(jogador);
    }

    public void tentarDefinirCapitao(Jogador jogador) {
        validarRequisitosCapitao(jogador);
        
        jogador.setCapitao(true);
        
        Capitao capitao = new Capitao(jogador);
        repository.salvarCapitao(capitao);
    }

    private void validarRequisitosCapitao(Jogador jogador) {
        if (!jogador.isContratoAtivo()) {
            throw new IllegalStateException("O jogador " + jogador.getNome() + " não possui contrato ativo.");
        }

        long meses = mesesNoClube(jogador);
        if (meses < 12) {
            throw new IllegalStateException("O jogador " + jogador.getNome() + " tem apenas " + meses + " meses de clube. Mínimo exigido: 12 meses.");
        }
    }

    public long mesesNoClube(Jogador jogador) {
        if (jogador.getChegadaNoClube() == null) return 0;
        return ChronoUnit.MONTHS.between(jogador.getChegadaNoClube(), LocalDate.now(clock));
    }
}