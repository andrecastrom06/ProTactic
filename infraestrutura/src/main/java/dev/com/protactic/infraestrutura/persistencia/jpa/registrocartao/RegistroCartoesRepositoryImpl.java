package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RegistroCartoesRepositoryImpl implements RegistroCartoesRepository, RegistroCartaoRepositorioAplicacao {

    private final RegistroCartoesRepositorySpringData repositoryJPA;
    
    private final JpaMapeador mapeador;

    public RegistroCartoesRepositoryImpl(RegistroCartoesRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    @Override
    public void salvarCartao(RegistroCartao cartao) {
        Objects.requireNonNull(cartao, "O RegistroCartao a ser salvo não pode ser nulo.");
        RegistroCartaoJPA jpa = mapeador.map(cartao, RegistroCartaoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de RegistroCartao para JPA não pode ser nulo.");
        repositoryJPA.save(jpa);
    }

    @Override
    public List<RegistroCartao> buscarCartoesPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta a ser buscado não pode ser nulo.");
        
        List<RegistroCartaoJPA> listaJPA = repositoryJPA.findByAtleta(atleta);
         
        return listaJPA.stream()
                .map(jpa -> mapeador.map(jpa, RegistroCartao.class))
                .collect(Collectors.toList());
    }

    @Override
    public void limparCartoes(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta para limpar cartões não pode ser nulo.");
        repositoryJPA.deleteByAtleta(atleta);
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        return repositoryJPA.findAllByAtleta(atleta);
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo) {
        Objects.requireNonNull(tipo, "O Tipo do cartão não pode ser nulo.");
        return repositoryJPA.findByTipo(tipo);
    }
    
}