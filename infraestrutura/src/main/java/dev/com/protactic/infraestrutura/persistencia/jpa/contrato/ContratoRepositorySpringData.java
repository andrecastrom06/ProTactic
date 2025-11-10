package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // 2. IMPORTAR LIST

@Repository
public interface ContratoRepositorySpringData extends JpaRepository<ContratoJPA, Integer> {

    List<ContratoResumo> findAllBy();


    List<ContratoResumo> findByClubeId(Integer clubeId);
}