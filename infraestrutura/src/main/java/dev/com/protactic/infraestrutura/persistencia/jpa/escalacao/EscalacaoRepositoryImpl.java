package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.repositorio.EscalacaoRepository;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class EscalacaoRepositoryImpl implements EscalacaoRepository, EscalacaoRepositorioAplicacao {

    private final EscalacaoSimplesRepositorySpringData repositoryJPA;

    public EscalacaoRepositoryImpl(EscalacaoSimplesRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }
    
    @Override
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador, Integer clubeId) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        Objects.requireNonNull(nomeJogador, "O 'nomeJogador' não pode ser nulo.");
        Objects.requireNonNull(clubeId, "O 'clubeId' não pode ser nulo.");
        
        EscalacaoSimplesJPA jpa = new EscalacaoSimplesJPA(jogoData, nomeJogador);
        jpa.setClubeId(clubeId); 
        repositoryJPA.save(jpa);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData, Integer clubeId) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        Objects.requireNonNull(clubeId, "O 'clubeId' não pode ser nulo.");
        
        return repositoryJPA.findByJogoDataAndClubeId(jogoData, clubeId)
            .stream()
            .map(EscalacaoSimplesJPA::getNomeJogador)
            .collect(Collectors.toList());
    }

    @Override
    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData, Integer clubeId) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        Objects.requireNonNull(clubeId, "O 'clubeId' não pode ser nulo.");
        return repositoryJPA.findAllByJogoDataAndClubeId(jogoData, clubeId);
    }
}