package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.jogador.JogadorRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class JogadorRepositoryImpl implements JogadorRepository, JogadorRepositorioAplicacao {

    private final JogadorRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public JogadorRepositoryImpl(JogadorRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (JogadorRepository) ---
    
    @Override
    public void salvar(Jogador jogador) {
        Objects.requireNonNull(jogador, "O Jogador a ser salvo não pode ser nulo.");
        JogadorJPA jogadorJPA = mapeador.map(jogador, JogadorJPA.class);
        Objects.requireNonNull(jogadorJPA, "O resultado do mapeamento de Jogador para JPA não pode ser nulo.");
        repositoryJPA.save(jogadorJPA);
    }

    @Override
    public Jogador buscarPorNome(String nome) {
        Objects.requireNonNull(nome, "O Nome do Jogador a ser buscado não pode ser nulo.");
        return repositoryJPA.findByNome(nome)
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .orElse(null);
    }

    @Override
    public boolean existe(String nome) {
        Objects.requireNonNull(nome, "O Nome do Jogador a ser verificado não pode ser nulo.");
        return repositoryJPA.existsByNome(nome);
    }

    @Override
    public List<Jogador> listarTodos() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .collect(Collectors.toList());
    }

    @Override
    public Jogador buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID do Jogador a ser buscado não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .orElse(null);
    }
    
    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (JogadorRepositorioAplicacao) ---

    /**
     * Este método implementa o contrato 'pesquisarResumos()'
     * da camada de Aplicação.
     * * Nota: Não precisamos do JpaMapeador. O Spring Data já
     * retorna a projeção (Resumo) pronta.
     */
    @Override
    public List<JogadorResumo> pesquisarResumos() {
        // 5. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findAllBy();
    }

    /**
     * Este método implementa o contrato 'pesquisarResumosPorClube(Integer clubeId)'
     * da camada de Aplicação.
     */
    @Override
    public List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId) {
        // 6. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findByClubeId(clubeId);
    }

    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}