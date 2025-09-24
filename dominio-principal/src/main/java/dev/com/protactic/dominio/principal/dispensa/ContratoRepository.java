package dev.com.protactic.dominio.principal.dispensa;

import java.util.List;

import dev.com.protactic.dominio.principal.Contrato;

public interface ContratoRepository {
    Contrato saveContrato(Contrato contrato);
    Contrato findContratoById(int id);
    List<Contrato> findAllContratos();
}