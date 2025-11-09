package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalacaoSimplesRepositorySpringData extends JpaRepository<EscalacaoSimplesJPA, Integer> {
    
    // --- Método para o Repositório do DOMÍNIO ---
    
    // 1. Implementa: obterEscalacaoPorData(String jogoData) -> List<String>
    List<EscalacaoSimplesJPA> findByJogoData(String jogoData);

    // --- MÉTODO NOVO (para o Repositório da APLICAÇÃO) ---

    /**
     * 2. Implementa: pesquisarResumosPorData(String jogoData) -> List<EscalacaoResumo>
     * O Spring Data vai criar a query e mapear o resultado
     * diretamente para a interface EscalacaoResumo.
     */
    List<EscalacaoResumo> findAllByJogoData(String jogoData);
}