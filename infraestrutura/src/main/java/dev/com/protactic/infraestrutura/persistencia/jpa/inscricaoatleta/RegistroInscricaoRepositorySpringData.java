package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

// 1. IMPORTAR O RESUMO E O LIST
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// O tipo da chave é a nossa classe 'InscricaoAtletaPK'
public interface RegistroInscricaoRepositorySpringData extends JpaRepository<InscricaoAtletaJPA, InscricaoAtletaPK> {
    
    // ... (Comentários sobre os métodos do JpaRepository) ...

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * Projeta todas as Inscrições para a interface InscricaoAtletaResumo.
     */
    List<InscricaoAtletaResumo> findAllBy();

    /**
     * Filtra por 'atleta' e projeta para InscricaoAtletaResumo.
     */
    List<InscricaoAtletaResumo> findByAtleta(String atleta);

    /**
     * Filtra por 'competicao' e projeta para InscricaoAtletaResumo.
     */
    List<InscricaoAtletaResumo> findByCompeticao(String competicao);
}