package dev.com.protactic.infraestrutura.persistencia.jpa.clube;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List; // 2. IMPORTAR LIST

@Repository
public interface ClubeRepositorySpringData extends JpaRepository<ClubeJPA, Integer> {
    
    // --- Método do Repositório do DOMÍNIO ---
    
    Optional<ClubeJPA> findByNome(String nome);

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * O Spring Data projeta todos os Clubes para a interface ClubeResumo.
     */
    List<ClubeResumo> findAllBy();

    /**
     * O Spring Data filtra por 'competicaoId' e projeta
     * o resultado para a interface ClubeResumo.
     */
    List<ClubeResumo> findByCompeticaoId(Integer competicaoId);
}