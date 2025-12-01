package dev.com.protactic.infraestrutura.persistencia.jpa.clube;


import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.clube.ClubeRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ClubeRepositoryImpl implements ClubeRepository, ClubeRepositorioAplicacao {

    private final ClubeRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public ClubeRepositoryImpl(ClubeRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    
    @Override
    public void salvar(Clube clube) {
        Objects.requireNonNull(clube, "O Clube a ser salvo não pode ser nulo.");
        ClubeJPA clubeJPA = mapeador.map(clube, ClubeJPA.class);
        Objects.requireNonNull(clubeJPA, "O resultado do mapeamento de Clube para JPA não pode ser nulo.");
        repositoryJPA.save(clubeJPA);
    }

    @Override
    public Clube buscarPorNome(String nome) {
        return repositoryJPA.findByNome(nome)
                .map(clubeJPA -> mapeador.map(clubeJPA, Clube.class))
                .orElse(null);
    }

    @Override
    public List<Clube> listarTodos() {
        return repositoryJPA.findAll().stream()
                .map(clubeJPA -> mapeador.map(clubeJPA, Clube.class))
                .collect(Collectors.toList());
    }

    @Override
    public Clube buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID do Clube a ser buscado não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(clubeJPA -> mapeador.map(clubeJPA, Clube.class))
                .orElse(null);
    }

    @Override
    public List<ClubeResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }


    @Override
    public List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId) {
        
        return repositoryJPA.findByCompeticaoId(competicaoId);
    }
    
}