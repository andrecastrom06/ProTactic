package dev.com.protactic.infraestrutura.persistencia.jpa.fisico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.Fisico;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.FisicoRepository;
import dev.com.protactic.aplicacao.principal.fisico.FisicoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FisicoRepositoryImpl implements FisicoRepository, FisicoRepositorioAplicacao {

    private final FisicoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;
    private final JogadorRepository jogadorRepository;

    public FisicoRepositoryImpl(FisicoRepositorySpringData repositoryJPA,
                                JpaMapeador mapeador,
                                JogadorRepository jogadorRepository) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
        this.jogadorRepository = jogadorRepository;
    }


    @Override
    public Fisico salvar(Fisico fisico) {
        Objects.requireNonNull(fisico, "O Treino Físico a ser salvo não pode ser nulo.");
        FisicoJPA jpa = mapeador.map(fisico, FisicoJPA.class);
        FisicoJPA salvo = repositoryJPA.save(jpa);
        return converterParaDominio(salvo);
    }

    @Override
    public Optional<Fisico> buscarPorId(Integer id) {
        return repositoryJPA.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Fisico> buscarPorJogadorId(Integer jogadorId) {
        return repositoryJPA.findByJogadorId(jogadorId).stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
    }


    @Override
    public List<FisicoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
    
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findAllByJogadorId(jogadorId);
    }


    private Fisico converterParaDominio(FisicoJPA jpa) {
        if (jpa == null) return null;
        Jogador jogador = jogadorRepository.buscarPorId(jpa.getJogadorId());
        if (jogador == null) {
            throw new RuntimeException("Jogador " + jpa.getJogadorId() + " não encontrado ao converter Treino Físico.");
        }
        
        return new Fisico(
            jpa.getId(),
            jogador,
            jpa.getNome(),
            jpa.getMusculo(),
            jpa.getIntensidade(),
            jpa.getDescricao(),
            jpa.getDataInicio(),
            jpa.getDataFim(),
            jpa.getStatus()
        );
    }
}