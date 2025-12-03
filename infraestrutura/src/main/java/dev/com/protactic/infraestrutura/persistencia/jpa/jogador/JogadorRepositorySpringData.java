package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional; 

@Repository
public interface JogadorRepositorySpringData extends JpaRepository<JogadorJPA, Integer> {

    List<JogadorJPA> findByNomeIgnoreCase(String nome);
    
    boolean existsByNome(String nome);

    List<JogadorResumo> findAllBy();

    List<JogadorResumo> findByClubeId(Integer clubeId);

    List<JogadorJPA> findAllByClubeId(Integer clubeId);

    Optional<JogadorJPA> findByNome(String nome);
}