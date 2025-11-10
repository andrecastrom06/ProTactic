package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

import dev.com.protactic.dominio.principal.Proposta;
import dev.com.protactic.dominio.principal.proposta.PropostaRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.proposta.PropostaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PropostaRepositoryImpl implements PropostaRepository, PropostaRepositorioAplicacao {

    private final PropostaRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public PropostaRepositoryImpl(PropostaRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    
    @Override
    public Proposta saveProposta(Proposta proposta) {
        Objects.requireNonNull(proposta, "A Proposta a ser salva não pode ser nula.");
        PropostaJPA propostaJPA = mapeador.map(proposta, PropostaJPA.class);
        Objects.requireNonNull(propostaJPA, "O resultado do mapeamento de Proposta para JPA não pode ser nulo.");
        
        PropostaJPA salvo = repositoryJPA.save(propostaJPA);
        return mapeador.map(salvo, Proposta.class);
    }

    @Override
    public Proposta findPropostaById(int id) {
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Proposta.class))
                .orElse(null);
    }

    @Override
    public List<Proposta> findAllPropostas() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, Proposta.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<PropostaResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<PropostaResumo> pesquisarResumosPorPropositor(Integer propositorId) {
        return repositoryJPA.findByPropositorId(propositorId);
    }

    @Override
    public List<PropostaResumo> pesquisarResumosPorReceptor(Integer receptorId) {
        return repositoryJPA.findByReceptorId(receptorId);
    }

    @Override
    public List<PropostaResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositoryJPA.findByJogadorId(jogadorId);
    }
    
}