package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

// O import da sua interface de Projeção (Resumo)
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalacaoSimplesRepositorySpringData extends JpaRepository<EscalacaoSimplesJPA, Integer> {
        
    // Método para o Domínio (usado em obterEscalacaoPorData)
    List<EscalacaoSimplesJPA> findByJogoData(String jogoData);

    // Método para a Aplicação (usado em pesquisarResumosPorData)
    List<EscalacaoResumo> findAllByJogoData(String jogoData);
}