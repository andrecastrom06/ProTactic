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


    @Query("SELECT rc FROM RegistroCartao rc WHERE rc.idJogador = :idJogador")
    List<RegistroCartaoJPA> findByIdJogador(@Param("idJogador") Integer idJogador);

    @Transactional
    @Modifying
    @Query("DELETE FROM RegistroCartao rc WHERE rc.idJogador = :idJogador")
    void deleteByIdJogador(@Param("idJogador") Integer idJogador);

    
    @Query("SELECT rc.id as id, j.nome as atleta, rc.tipo as tipo " +
           "FROM RegistroCartao rc JOIN Jogador j ON rc.idJogador = j.id")
    List<RegistroCartaoResumo> findAllResumos();

    @Query("SELECT rc.id as id, j.nome as atleta, rc.tipo as tipo " +
           "FROM RegistroCartao rc JOIN Jogador j ON rc.idJogador = j.id " +
           "WHERE rc.idJogador = :idJogador")
    List<RegistroCartaoResumo> findAllResumosByIdJogador(@Param("idJogador") Integer idJogador);

    @Query("SELECT rc.id as id, j.nome as atleta, rc.tipo as tipo " +
           "FROM RegistroCartao rc JOIN Jogador j ON rc.idJogador = j.id " +
           "WHERE rc.tipo = :tipo")
    List<RegistroCartaoResumo> findResumosByTipo(@Param("tipo") String tipo);
}