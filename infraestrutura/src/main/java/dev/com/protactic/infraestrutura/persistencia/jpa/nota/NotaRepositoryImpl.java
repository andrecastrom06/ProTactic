package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Nota;
import dev.com.protactic.dominio.principal.nota.NotaRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.nota.NotaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.nota.NotaResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List; // Importar List
import java.util.Objects;
import java.util.Optional;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class NotaRepositoryImpl implements NotaRepository, NotaRepositorioAplicacao {

    private final NotaRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public NotaRepositoryImpl(NotaRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (NotaRepository) ---
    
    @Override
    public Optional<Nota> buscar(String jogoId, String jogadorId) {
        NotaPK pk = new NotaPK(jogoId, jogadorId);
        return repositoryJPA.findById(pk)
                .map(jpa -> mapeador.map(jpa, Nota.class));
    }

    @Override
    public void salvar(Nota nota) {
        Objects.requireNonNull(nota, "A Nota a ser salva não pode ser nula.");
        NotaJPA notaJPA = mapeador.map(nota, NotaJPA.class);
        Objects.requireNonNull(notaJPA, "O resultado do mapeamento de Nota para JPA não pode ser nulo.");
        repositoryJPA.save(notaJPA);
    }

    @Override
    public void limpar() {
        repositoryJPA.deleteAll();
    }

    // --- Métodos 'TODO' do Domínio (Não mexer) ---

    @Override
    public void registrarParticipacao(String jogoId, String jogadorId, boolean atuou) {
        throw new UnsupportedOperationException("Implementação de 'registrarParticipacao' pendente.");
    }
    // ... (restante dos métodos 'TODO') ...
    @Override
    public boolean atuouNoJogo(String jogoId, String jogadorId) {
        System.err.println("AVISO: 'atuouNoJogo' está a retornar 'true' por padrão. Implementação pendente.");
        return true; 
    }
    @Override
    public void registrarJogadorNoElenco(String jogadorId) {
        throw new UnsupportedOperationException("Implementação de 'registrarJogadorNoElenco' pendente.");
    }
    @Override
    public boolean jogadorExisteNoElenco(String jogadorId) {
        throw new UnsupportedOperationException("Implementação de 'jogadorExisteNoElenco' pendente.");
    }

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (NotaRepositorioAplicacao) ---

    /**
     * Implementa o 'pesquisarResumosPorJogo(String jogoId)' da Aplicação.
     */
    @Override
    public List<NotaResumo> pesquisarResumosPorJogo(String jogoId) {
        // 5. Simplesmente chamamos o método do Spring Data
        // que já retorna a projeção pronta.
        return repositoryJPA.findByJogoId(jogoId);
    }

    /**
     * Implementa o 'pesquisarResumosPorJogador(String jogadorId)' da Aplicação.
     */
    @Override
    public List<NotaResumo> pesquisarResumosPorJogador(String jogadorId) {
        // 6. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findByJogadorId(jogadorId);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}