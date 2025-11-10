package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import dev.com.protactic.aplicacao.principal.nota.NotaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepositorySpringData extends JpaRepository<NotaJPA, NotaPK> {

    List<NotaResumo> findByJogoId(String jogoId);

    List<NotaResumo> findByJogadorId(String jogadorId);
}