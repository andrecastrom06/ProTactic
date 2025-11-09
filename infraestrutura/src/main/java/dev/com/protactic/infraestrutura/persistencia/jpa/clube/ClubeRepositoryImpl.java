package dev.com.protactic.infraestrutura.persistencia.jpa.clube;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.clube.ClubeRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;

// 3. Importa as classes de infraestrutura do Spring
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class ClubeRepositoryImpl implements ClubeRepository, ClubeRepositorioAplicacao {

    private final ClubeRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public ClubeRepositoryImpl(ClubeRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (ClubeRepository) ---
    
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

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (ClubeRepositorioAplicacao) ---

    /**
     * Implementa o 'pesquisarResumos()' da Aplicação.
     */
    @Override
    public List<ClubeResumo> pesquisarResumos() {
        // 5. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findAllBy();
    }

    /**
     * Implementa o 'pesquisarResumosPorCompeticao(Integer competicaoId)' da Aplicação.
     */
    @Override
    public List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId) {
        // 6. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findByCompeticaoId(competicaoId);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}