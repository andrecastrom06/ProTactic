package dev.com.protactic.dominio.principal;

import java.util.List;

public interface ContratoRepository {
    Contrato save(Contrato contrato);
    Contrato findById(int id);
    List<Contrato> findAll();
}