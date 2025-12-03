package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio.PremiacaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class PremiacaoRepositoryImpl implements PremiacaoRepository, PremiacaoRepositorioAplicacao {

    private final PremiacaoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public PremiacaoRepositoryImpl(PremiacaoRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        // Inicializa com valor ZERO
        return new Premiacao(0, null, nomePremiacao, dataPremiacao, BigDecimal.ZERO);
    }

    @Override
    public void salvarPremiacao(Premiacao premiacao) {
        Objects.requireNonNull(premiacao, "A Premiacao a ser salva não pode ser nula.");
        
        // Mapeia a Entidade de Domínio para a Entidade JPA
        PremiacaoJPA jpa = mapeador.map(premiacao, PremiacaoJPA.class);
        
       
        if (jpa.getId() != null && jpa.getId() == 0) {
            jpa.setId(null);
        }
        
        Objects.requireNonNull(jpa, "O resultado do mapeamento de Premiacao para JPA não pode ser nulo.");
        
        // Salva e recupera a entidade com o novo ID gerado
        jpa = repositoryJPA.save(jpa);
        
        // Atualiza o ID no objeto original de domínio (opcional, mas boa prática)
        premiacao.setId(jpa.getId());
    }

    @Override
    public List<PremiacaoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findByJogadorId(jogadorId);
    }

    @Override
    public List<PremiacaoResumo> pesquisarResumosPorNome(String nome) {
        Objects.requireNonNull(nome, "O Nome da premiação não pode ser nulo.");
        return repositoryJPA.findByNome(nome);
    }
}