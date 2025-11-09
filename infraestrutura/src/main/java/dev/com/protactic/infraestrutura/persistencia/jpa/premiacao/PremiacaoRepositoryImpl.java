package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List; // Importar List
import java.util.Objects;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class PremiacaoRepositoryImpl implements PremiacaoRepository, PremiacaoRepositorioAplicacao {

    private final PremiacaoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public PremiacaoRepositoryImpl(PremiacaoRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (PremiacaoRepository) ---
    
    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        // Este método permanece como estava (Fábrica do Domínio)
        return new Premiacao(0, null, nomePremiacao, dataPremiacao);
    }

    @Override
    public void salvarPremiacao(Premiacao premiacao) {
        Objects.requireNonNull(premiacao, "A Premiacao a ser salva não pode ser nula.");
        PremiacaoJPA jpa = mapeador.map(premiacao, PremiacaoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de Premiacao para JPA não pode ser nulo.");
        repositoryJPA.save(jpa);
    }

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (PremiacaoRepositorioAplicacao) ---

    @Override
    public List<PremiacaoResumo> pesquisarResumos() {
        // 5. Chama o novo método que retorna a Projeção
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        // 6. Chama o novo método que retorna a Projeção
        return repositoryJPA.findByJogadorId(jogadorId);
    }

    @Override
    public List<PremiacaoResumo> pesquisarResumosPorNome(String nome) {
        Objects.requireNonNull(nome, "O Nome da premiação não pode ser nulo.");
        // 7. Chama o novo método que retorna a Projeção
        return repositoryJPA.findByNome(nome);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}