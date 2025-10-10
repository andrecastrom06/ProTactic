package dev.com.protactic.mocks;

import java.util.*;

import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.dispensa.ContratoRepository;

public class ContratoMock implements ContratoRepository {
    private final Map<Integer, Contrato> storage = new HashMap<>();
    private int sequence = 1;

    @Override
    public Contrato saveContrato(Contrato contrato) {
        if (contrato.getId() == 0) {
            contrato.setId(sequence++);
        }
        storage.put(contrato.getId(), contrato);
        return contrato;
    }

    @Override
    public Contrato findContratoById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Contrato> findAllContratos() {
        return new ArrayList<>(storage.values());
    }
}