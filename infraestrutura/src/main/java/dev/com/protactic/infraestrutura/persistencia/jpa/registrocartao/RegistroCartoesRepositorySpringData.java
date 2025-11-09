package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

// 1. IMPORTAR O RESUMO DA APLICAÇÃO
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RegistroCartoesRepositorySpringData extends JpaRepository<RegistroCartaoJPA, Integer> {
    
    // --- Métodos para o Repositório do DOMÍNIO ---

    // 1. Implementa: buscarCartoesPorAtleta(String atleta) -> Retorna Entidade JPA
    List<RegistroCartaoJPA> findByAtleta(String atleta);

    // 2. Implementa: limparCartoes(String atleta)
    @Transactional
    @Modifying
    @Query("DELETE FROM RegistroCartao rc WHERE rc.atleta = :atleta")
    void deleteByAtleta(@Param("atleta") String atleta);


    // --- MÉTODOS NOVOS (para o Repositório da APLICAÇÃO) ---

    /**
     * 3. Projeta todos os Registros para a interface RegistroCartaoResumo.
     */
    List<RegistroCartaoResumo> findAllBy();

    /**
     * 4. Filtra por 'atleta' e projeta os resultados para RegistroCartaoResumo.
     * (Nome diferente de 'findByAtleta' para evitar ambiguidade de retorno)
     */
    List<RegistroCartaoResumo> findAllByAtleta(String atleta);

    /**
     * 5. Filtra por 'tipo' e projeta os resultados para RegistroCartaoResumo.
     */
    List<RegistroCartaoResumo> findByTipo(String tipo);
}