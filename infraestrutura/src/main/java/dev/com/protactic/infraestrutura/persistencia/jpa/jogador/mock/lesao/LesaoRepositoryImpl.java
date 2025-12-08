package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.LesaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.lesao.LesaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LesaoRepositoryImpl implements LesaoRepository, LesaoRepositorioAplicacao {

    private final LesaoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;
    private final JogadorRepository jogadorRepository;

    public LesaoRepositoryImpl(LesaoRepositorySpringData repositoryJPA, 
                               JpaMapeador mapeador,
                               JogadorRepository jogadorRepository) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
        this.jogadorRepository = jogadorRepository;
    }

    @Override
    public Lesao salvar(Lesao lesao) {
        Objects.requireNonNull(lesao, "A Lesao a ser salva não pode ser nula.");
        LesaoJPA jpa = mapeador.map(lesao, LesaoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de Lesao para JPA não pode ser nulo.");
        LesaoJPA salvo = repositoryJPA.save(jpa);
        return converterParaDominio(salvo);
    }

    @Override
    public Optional<Lesao> buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID da Lesao não pode ser nulo.");
        return repositoryJPA.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Lesao> buscarTodasPorJogadorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findByJogadorId(jogadorId).stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findAtivaByJogadorId(jogadorId)
                .map(this::converterParaDominio);
    }

    private Lesao converterParaDominio(LesaoJPA jpa) {
        if (jpa == null) return null;
        Jogador jogador = jogadorRepository.buscarPorId(jpa.getJogadorId());
        return new Lesao(
            jpa.getId(),
            jogador, 
            jpa.isLesionado(),
            jpa.getTempo(),
            jpa.getPlano(),
            jpa.getGrau()
        );
    }

    @Override
    public List<LesaoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findAllByJogadorId(jogadorId);
    }

    @Override
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findResumoAtivoByJogadorId(jogadorId);
    }
    
}