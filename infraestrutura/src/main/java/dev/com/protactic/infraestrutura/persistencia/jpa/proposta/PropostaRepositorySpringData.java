package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

import dev.com.protactic.aplicacao.principal.proposta.PropostaResumo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepositorySpringData extends JpaRepository<PropostaJPA, Integer> {
    
    List<PropostaResumo> findAllBy();

    List<PropostaResumo> findByPropositorId(Integer propositorId);

    List<PropostaResumo> findByReceptorId(Integer receptorId);

    List<PropostaResumo> findByJogadorId(Integer jogadorId);
}