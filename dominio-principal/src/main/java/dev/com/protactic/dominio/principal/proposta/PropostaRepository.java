package dev.com.protactic.dominio.principal.proposta;

import java.util.List;

import dev.com.protactic.dominio.principal.Proposta;

public interface PropostaRepository {
    Proposta saveProposta(Proposta proposta);
    Proposta findPropostaById(int id);
    List<Proposta> findAllPropostas();
}