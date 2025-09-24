package dev.com.protactic.dominio.principal.proposta;

import java.util.List;

import dev.com.protactic.dominio.principal.Proposta;

public interface PropostaRepository {
    Proposta save(Proposta proposta);
    Proposta findById(int id);
    List<Proposta> findAll();
}