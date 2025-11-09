package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

// 1. IMPORTAR O RESUMO E O LIST
import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepositorySpringData extends JpaRepository<PropostaJPA, Integer> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * Projeta todas as Propostas para a interface PropostaResumo.
     */
    List<PropostaResumo> findAllBy();

    /**
     * Filtra por 'propositorId' e projeta para PropostaResumo.
     */
    List<PropostaResumo> findByPropositorId(Integer propositorId);

    /**
     * Filtra por 'receptorId' e projeta para PropostaResumo.
     */
    List<PropostaResumo> findByReceptorId(Integer receptorId);

    /**
     * Filtra por 'jogadorId' e projeta para PropostaResumo.
     */
    List<PropostaResumo> findByJogadorId(Integer jogadorId);
}