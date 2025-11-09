package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

// 1. IMPORTAR O RESUMO E O LIST
import dev.com.protactic.aplicacao.principal.nota.NotaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// O tipo da chave é a nossa classe 'NotaPK'
public interface NotaRepositorySpringData extends JpaRepository<NotaJPA, NotaPK> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * O Spring Data entende que deve filtrar por 'jogoId'
     * e projetar o resultado para a interface NotaResumo.
     */
    List<NotaResumo> findByJogoId(String jogoId);

    /**
     * O Spring Data entende que deve filtrar por 'jogadorId'
     * e projetar o resultado para a interface NotaResumo.
     */
    List<NotaResumo> findByJogadorId(String jogadorId);
}