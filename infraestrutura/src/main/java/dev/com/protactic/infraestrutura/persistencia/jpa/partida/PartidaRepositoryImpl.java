package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

// 1. IMPORTA O "CONTRATO" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.partida.PartidaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.partida.PartidaResumo;

// 2. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
// 3. Implementa *apenas* a interface da Aplicação
public class PartidaRepositoryImpl implements PartidaRepositorioAplicacao {

    private final PartidaRepositorySpringData repositoryJPA;

    // 4. Nota: Não precisamos do JpaMapeador aqui, pois o Spring Data
    // já retorna as Projeções (Resumos) prontas.
    public PartidaRepositoryImpl(PartidaRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (PartidaRepositorioAplicacao) ---

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