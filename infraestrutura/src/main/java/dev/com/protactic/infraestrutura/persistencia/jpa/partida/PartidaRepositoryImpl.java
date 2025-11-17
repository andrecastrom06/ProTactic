package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

import dev.com.protactic.aplicacao.principal.partida.PartidaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.partida.PartidaRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional; 

@Component
public class PartidaRepositoryImpl implements PartidaRepositorioAplicacao, PartidaRepository { 

    private final PartidaRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador; 

    public PartidaRepositoryImpl(PartidaRepositorySpringData repositoryJPA, JpaMapeador mapeador) { 
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador; 
    }

    @Override
    public Optional<Partida> buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID da Partida não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Partida.class));
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