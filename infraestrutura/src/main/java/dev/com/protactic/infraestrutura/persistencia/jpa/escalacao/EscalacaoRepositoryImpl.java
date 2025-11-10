package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.dominio.principal.definirEsquemaTatico.EscalacaoRepository;

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;

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
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        Objects.requireNonNull(nomeJogador, "O 'nomeJogador' não pode ser nulo.");
        
        EscalacaoSimplesJPA jpa = new EscalacaoSimplesJPA(jogoData, nomeJogador);
        repositoryJPA.save(jpa);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        
        List<EscalacaoSimplesJPA> jpaList = repositoryJPA.findByJogoData(jogoData);
        
        return jpaList.stream()
                .map(EscalacaoSimplesJPA::getNomeJogador)
                .collect(Collectors.toList());
    }

    @Override
    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        
        return repositoryJPA.findAllByJogoData(jogoData);
    }
    

}