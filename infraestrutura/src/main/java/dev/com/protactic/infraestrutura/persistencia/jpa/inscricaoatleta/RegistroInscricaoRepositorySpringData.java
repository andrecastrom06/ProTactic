package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroInscricaoRepositorySpringData extends JpaRepository<InscricaoAtletaJPA, InscricaoAtletaPK> {
 
    List<InscricaoAtletaResumo> findAllBy();

    List<InscricaoAtletaResumo> findByAtleta(String atleta);

    List<InscricaoAtletaResumo> findByCompeticao(String competicao);
}