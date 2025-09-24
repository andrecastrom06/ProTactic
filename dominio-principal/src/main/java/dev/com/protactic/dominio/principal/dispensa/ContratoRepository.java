package dev.com.protactic.dominio.principal.dispensa;

import java.util.List;

import dev.com.protactic.dominio.principal.Contrato;

public interface ContratoRepository {
    Contrato save(Contrato contrato);
    Contrato findById(int id);
    List<Contrato> findAll();
}