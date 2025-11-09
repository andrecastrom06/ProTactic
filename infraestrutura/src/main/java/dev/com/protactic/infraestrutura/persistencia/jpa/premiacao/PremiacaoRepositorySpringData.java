package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

// 1. IMPORTAR O RESUMO E O LIST
import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiacaoRepositorySpringData extends JpaRepository<PremiacaoJPA, Integer> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * Projeta todas as Premiações para a interface PremiacaoResumo.
     */
    List<PremiacaoResumo> findAllBy();

    /**
     * Filtra por 'jogadorId' e projeta para PremiacaoResumo.
     */
    List<PremiacaoResumo> findByJogadorId(Integer jogadorId);

    /**
     * Filtra por 'nome' e projeta para PremiacaoResumo.
     */
    List<PremiacaoResumo> findByNome(String nome);
}