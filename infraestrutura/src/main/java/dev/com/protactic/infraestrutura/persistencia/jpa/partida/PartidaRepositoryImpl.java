package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

import dev.com.protactic.aplicacao.principal.partida.PartidaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
public class PartidaRepositoryImpl implements PartidaRepositorioAplicacao {

    private final PartidaRepositorySpringData repositoryJPA;

    public PartidaRepositoryImpl(PartidaRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }


    @Override
    public List<PartidaResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<PartidaResumo> pesquisarResumosPorClubeCasa(Integer clubeCasaId) {
        Objects.requireNonNull(clubeCasaId, "O ID do Clube da Casa não pode ser nulo.");
        return repositoryJPA.findByClubeCasaId(clubeCasaId);
    }

    @Override
    public List<PartidaResumo> pesquisarResumosPorClubeVisitante(Integer clubeVisitanteId) {
        Objects.requireNonNull(clubeVisitanteId, "O ID do Clube Visitante não pode ser nulo.");
        return repositoryJPA.findByClubeVisitanteId(clubeVisitanteId);
    }
}