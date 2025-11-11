package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; 

@Repository
public interface JogadorRepositorySpringData extends JpaRepository<JogadorJPA, Integer> {
    
    // --- (INÍCIO DA CORREÇÃO) ---
    // Alteramos de 'Optional<JogadorJPA>' para 'List<JogadorJPA>'
    // Isto permite que a query retorne múltiplos jogadores com o mesmo nome.
    List<JogadorJPA> findByNome(String nome);
    // --- (FIM DA CORREÇÃO) ---
    
    boolean existsByNome(String nome);

    List<JogadorResumo> findAllBy();

    List<JogadorResumo> findByClubeId(Integer clubeId);
}