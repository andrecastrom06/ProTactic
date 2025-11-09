package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

// 2. IMPORTA OS "CONTRATOS" E "RESUMOS" DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.contrato.ContratoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;

// 3. Importa as classes de infraestrutura
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador; // Importar o Mapeador
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ContratoRepositoryImpl implements ContratoRepository, ContratoRepositorioAplicacao {

    private final ContratoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public ContratoRepositoryImpl(ContratoRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    // --- Implementação dos métodos da interface do DOMÍNIO (ContratoRepository) ---
    
    @Override
    public void salvar(Contrato contrato) {
        Objects.requireNonNull(contrato, "O Contrato a ser salvo não pode ser nulo.");
        
        ContratoJPA contratoJPA = mapeador.map(contrato, ContratoJPA.class);
        
        Objects.requireNonNull(contratoJPA, "O resultado do mapeamento de Contrato para JPA não pode ser nulo.");
        
        // --- (INÍCIO DA CORREÇÃO) ---
        // 1. Salvamos a entidade JPA E guardamos o resultado
        //    (O 'entidadeSalva' terá o novo ID gerado pelo banco)
        ContratoJPA entidadeSalva = repositoryJPA.save(contratoJPA);
        
        // 2. Atualizamos o ID do objeto de domínio original
        //    (Como o Java passa objetos por referência,
        //    o 'CadastroDeAtletaService' verá esta mudança!)
        contrato.setId(entidadeSalva.getId());
        // --- (FIM DA CORREÇÃO) ---
    }

    @Override
    public Contrato buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID do Contrato a ser buscado não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Contrato.class))
                .orElse(null);
    }

    @Override
    public List<Contrato> listarTodos() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, Contrato.class))
                .collect(Collectors.toList());
    }

    // --- (FIM) Implementação dos métodos do DOMÍNIO ---


    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA APLICAÇÃO (ContratoRepositorioAplicacao) ---

    @Override
    public List<ContratoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositoryJPA.findByClubeId(clubeId);
    }
}