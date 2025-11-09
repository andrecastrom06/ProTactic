package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // 2. IMPORTAR LIST

@Repository
public interface ContratoRepositorySpringData extends JpaRepository<ContratoJPA, Integer> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * O Spring Data projeta todos os Contratos para a interface ContratoResumo.
     */
    List<ContratoResumo> findAllBy();

    /**
     * O Spring Data filtra por 'clubeId' e projeta
     * o resultado para a interface ContratoResumo.
     */
    List<ContratoResumo> findByClubeId(Integer clubeId);
}