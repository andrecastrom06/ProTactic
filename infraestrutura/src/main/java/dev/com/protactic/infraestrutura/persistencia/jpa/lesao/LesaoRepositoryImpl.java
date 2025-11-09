package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.lesao.LesaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class LesaoRepositoryImpl implements LesaoRepository, LesaoRepositorioAplicacao {

    private final LesaoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;
    private final JogadorRepository jogadorRepository;

    public LesaoRepositoryImpl(LesaoRepositorySpringData repositoryJPA, 
                               JpaMapeador mapeador,
                               JogadorRepository jogadorRepository) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
        this.jogadorRepository = jogadorRepository;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (LesaoRepository) ---
    // (Esta parte usa o conversor 'inteligente')
    
    @Override
    public Lesao salvar(Lesao lesao) {
        Objects.requireNonNull(lesao, "A Lesao a ser salva não pode ser nula.");
        LesaoJPA jpa = mapeador.map(lesao, LesaoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de Lesao para JPA não pode ser nulo.");
        LesaoJPA salvo = repositoryJPA.save(jpa);
        return converterParaDominio(salvo);
    }

    @Override
    public Optional<Lesao> buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID da Lesao não pode ser nulo.");
        return repositoryJPA.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Lesao> buscarTodasPorJogadorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        // 5. Usa o método que retorna a Entidade JPA
        return repositoryJPA.findByJogadorId(jogadorId).stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lesao> buscarAtivaPorJogadorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        // 6. Usa o método que retorna a Entidade JPA
        return repositoryJPA.findAtivaByJogadorId(jogadorId)
                .map(this::converterParaDominio);
    }

    private Lesao converterParaDominio(LesaoJPA jpa) {
        if (jpa == null) return null;
        Jogador jogador = jogadorRepository.buscarPorId(jpa.getJogadorId());
        return new Lesao(
            jpa.getId(),
            jogador, 
            jpa.isLesionado(),
            jpa.getTempo(),
            jpa.getPlano(),
            jpa.getGrau()
        );
    }
    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (LesaoRepositorioAplicacao) ---
    // (Esta parte é "burra" e simples, apenas chama as projeções)

    @Override
    public List<LesaoResumo> pesquisarResumos() {
        // 7. Chama o novo método que retorna a Projeção
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        // 8. Chama o novo método que retorna a Projeção
        return repositoryJPA.findAllByJogadorId(jogadorId);
    }

    @Override
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        // 9. Chama o novo método que retorna a Projeção
        return repositoryJPA.findResumoAtivoByJogadorId(jogadorId);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}