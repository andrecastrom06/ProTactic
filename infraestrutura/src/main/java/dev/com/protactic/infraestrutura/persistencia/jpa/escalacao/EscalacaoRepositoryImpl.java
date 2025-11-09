package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

// 1. Importa o "contrato" do DOMÍNIO
import dev.com.protactic.dominio.principal.definirEsquemaTatico.EscalacaoRepository;

// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class EscalacaoRepositoryImpl implements EscalacaoRepository, EscalacaoRepositorioAplicacao {

    private final EscalacaoSimplesRepositorySpringData repositoryJPA;

    public EscalacaoRepositoryImpl(EscalacaoSimplesRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (EscalacaoRepository) ---
    
    @Override
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        Objects.requireNonNull(nomeJogador, "O 'nomeJogador' não pode ser nulo.");
        
        EscalacaoSimplesJPA jpa = new EscalacaoSimplesJPA(jogoData, nomeJogador);
        repositoryJPA.save(jpa);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        
        // 5. Usa o método que retorna a Entidade JPA
        List<EscalacaoSimplesJPA> jpaList = repositoryJPA.findByJogoData(jogoData);
        
        return jpaList.stream()
                .map(EscalacaoSimplesJPA::getNomeJogador)
                .collect(Collectors.toList());
    }

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (EscalacaoRepositorioAplicacao) ---

    /**
     * Implementa o 'pesquisarResumosPorData(String jogoData)' da Aplicação.
     * * Nota: Não precisamos de mapeamento manual. O Spring Data
     * já retorna a projeção (Resumo) pronta.
     */
    @Override
    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData) {
        Objects.requireNonNull(jogoData, "O 'jogoData' não pode ser nulo.");
        
        // 6. Usa o novo método que retorna a Projeção
        return repositoryJPA.findAllByJogoData(jogoData);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}