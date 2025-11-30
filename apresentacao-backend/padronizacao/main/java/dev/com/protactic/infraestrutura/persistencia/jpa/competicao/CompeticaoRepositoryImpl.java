package dev.com.protactic.infraestrutura.persistencia.jpa.competicao;

import dev.com.protactic.aplicacao.principal.competicao.CompeticaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.competicao.CompeticaoResumo;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CompeticaoRepositoryImpl implements CompeticaoRepositorioAplicacao {

    private final CompeticaoRepositorySpringData repositoryJPA;

    public CompeticaoRepositoryImpl(CompeticaoRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    public List<CompeticaoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }
}