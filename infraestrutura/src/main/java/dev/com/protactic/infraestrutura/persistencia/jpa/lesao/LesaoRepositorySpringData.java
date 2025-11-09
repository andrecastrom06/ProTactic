package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.lesao.LesaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LesaoRepositorySpringData extends JpaRepository<LesaoJPA, Integer> {
    
    // --- Métodos para o Repositório do DOMÍNIO ---

    // 1. Implementa: buscarTodasPorJogadorId(Integer jogadorId) -> Retorna Entidade JPA
    List<LesaoJPA> findByJogadorId(Integer jogadorId);

    // 2. Implementa: buscarAtivaPorJogadorId(Integer jogadorId) -> Retorna Entidade JPA
    @Query("SELECT l FROM Lesao l WHERE l.jogadorId = :jogadorId AND l.lesionado = true")
    Optional<LesaoJPA> findAtivaByJogadorId(@Param("jogadorId") Integer jogadorId);


    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * 3. Projeta todas as Lesoes para a interface LesaoResumo.
     */
    List<LesaoResumo> findAllBy();

    /**
     * 4. Filtra por 'jogadorId' e projeta os resultados para LesaoResumo.
     * (Nome diferente de 'findByJogadorId' para evitar ambiguidade de retorno)
     */
    List<LesaoResumo> findAllByJogadorId(Integer jogadorId);

    /**
     * 5. Busca a lesão ativa e projeta o resultado para LesaoResumo.
     * (Nome diferente de 'findAtivaByJogadorId' para evitar ambiguidade)
     */
    @Query("SELECT l FROM Lesao l WHERE l.jogadorId = :jogadorId AND l.lesionado = true")
    Optional<LesaoResumo> findResumoAtivoByJogadorId(@Param("jogadorId") Integer jogadorId);
}