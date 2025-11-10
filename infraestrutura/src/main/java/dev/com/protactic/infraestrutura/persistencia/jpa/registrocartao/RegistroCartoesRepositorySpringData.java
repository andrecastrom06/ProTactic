package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

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

    List<RegistroCartaoJPA> findByAtleta(String atleta);

    @Transactional
    @Modifying
    @Query("DELETE FROM RegistroCartao rc WHERE rc.atleta = :atleta")
    void deleteByAtleta(@Param("atleta") String atleta);

    List<RegistroCartaoResumo> findAllBy();

    List<RegistroCartaoResumo> findAllByAtleta(String atleta);

    List<RegistroCartaoResumo> findByTipo(String tipo);
}