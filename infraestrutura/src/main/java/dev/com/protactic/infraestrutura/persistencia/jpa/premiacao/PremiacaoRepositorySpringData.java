package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import dev.com.protactic.aplicacao.principal.premiacao.PremiacaoResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiacaoRepositorySpringData extends JpaRepository<PremiacaoJPA, Integer> {

    List<PremiacaoResumo> findAllBy();

    List<PremiacaoResumo> findByJogadorId(Integer jogadorId);

    List<PremiacaoResumo> findByNome(String nome);
}