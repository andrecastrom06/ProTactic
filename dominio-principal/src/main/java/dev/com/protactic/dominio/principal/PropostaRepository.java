package dev.com.protactic.dominio.principal;

import java.util.List;

public interface PropostaRepository {
    Proposta save(Proposta proposta);
    Proposta findById(int id);
    List<Proposta> findAll();
}