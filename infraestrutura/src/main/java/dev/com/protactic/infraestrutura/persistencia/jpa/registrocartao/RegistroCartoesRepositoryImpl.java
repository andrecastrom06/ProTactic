package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;

// 3. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
// 4. FAZEMOS A CLASSE IMPLEMENTAR AMBAS AS INTERFACES
public class RegistroCartoesRepositoryImpl implements RegistroCartoesRepository, RegistroCartaoRepositorioAplicacao {

    private final RegistroCartoesRepositorySpringData repositoryJPA;
    
    // --- INÍCIO DA CORREÇÃO ---
    // O nome do tipo estava errado (JpaMadeador)
    private final JpaMapeador mapeador;
    // --- FIM DA CORREÇÃO ---

    // O construtor já estava correto
    public RegistroCartoesRepositoryImpl(RegistroCartoesRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (RegistroCartoesRepository) ---
    
    @Override
    public void salvarCartao(RegistroCartao cartao) {
        Objects.requireNonNull(cartao, "O RegistroCartao a ser salvo não pode ser nulo.");
        RegistroCartaoJPA jpa = mapeador.map(cartao, RegistroCartaoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de RegistroCartao para JPA não pode ser nulo.");
        repositoryJPA.save(jpa);
    }

    @Override
    public List<RegistroCartao> buscarCartoesPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta a ser buscado não pode ser nulo.");
        
        // 5. Usa o método que retorna a Entidade JPA
        List<RegistroCartaoJPA> listaJPA = repositoryJPA.findByAtleta(atleta);
        
        // Este 'mapeador' agora é do tipo correto e o erro deve desaparecer
        return listaJPA.stream()
                .map(jpa -> mapeador.map(jpa, RegistroCartao.class))
                .collect(Collectors.toList());
    }

    @Override
    public void limparCartoes(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta para limpar cartões não pode ser nulo.");
        repositoryJPA.deleteByAtleta(atleta);
    }

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (RegistroCartaoRepositorioAplicacao) ---

    @Override
    public List<RegistroCartaoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        return repositoryJPA.findAllByAtleta(atleta);
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo) {
        Objects.requireNonNull(tipo, "O Tipo do cartão não pode ser nulo.");
        return repositoryJPA.findByTipo(tipo);
    }
    
    // --- (FIM) Implementação dos métodos da APLICAÇÃO ---
}