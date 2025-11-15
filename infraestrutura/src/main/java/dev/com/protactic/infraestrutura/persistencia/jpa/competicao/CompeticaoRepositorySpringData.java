package dev.com.protactic.infraestrutura.persistencia.jpa.competicao;

import dev.com.protactic.aplicacao.principal.competicao.CompeticaoResumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompeticaoRepositorySpringData extends JpaRepository<CompeticaoJPA, Integer> {
    
    // Método de projeção para a Aplicação
    List<CompeticaoResumo> findAllBy();
}