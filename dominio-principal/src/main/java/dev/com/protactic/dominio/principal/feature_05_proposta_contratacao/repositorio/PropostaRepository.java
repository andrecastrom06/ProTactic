package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Proposta;

public interface PropostaRepository {
    Proposta saveProposta(Proposta proposta);
    Proposta findPropostaById(int id);
    List<Proposta> findAllPropostas();
    void deleteProposta(Proposta proposta); 
}