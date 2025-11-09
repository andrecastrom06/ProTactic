package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

// 1. IMPORTAMOS O RESUMO DA CAMADA DE APLICAÇÃO
import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List; // Importar java.util.List

@Repository
public interface JogadorRepositorySpringData extends JpaRepository<JogadorJPA, Integer> {
    
    // --- Métodos do Repositório do DOMÍNIO ---
    
    // O Spring cria a query para: buscarPorNome(String nome)
    Optional<JogadorJPA> findByNome(String nome);
    
    // O Spring cria a query para: existe(String nome)
    boolean existsByNome(String nome);

    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * O Spring Data entende que queremos todos os JogadorJPA,
     * mas projetados (mapeados) para a interface JogadorResumo.
     * Ele faz a query otimizada automaticamente.
     */
    List<JogadorResumo> findAllBy();

    /**
     * O Spring Data filtra por 'clubeId' e projeta
     * o resultado para a interface JogadorResumo.
     */
    List<JogadorResumo> findByClubeId(Integer clubeId);
}