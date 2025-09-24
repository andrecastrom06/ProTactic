package dev.com.protactic.dominio.principal;

import java.util.*;

public class ContratoMock implements ContratoRepository {
    private final Map<Integer, Contrato> storage = new HashMap<>();
    private int sequence = 1;

    @Override
    public Contrato save(Contrato contrato) {
        if (contrato.getId() == 0) {
            contrato.setId(sequence++);
        }
        storage.put(contrato.getId(), contrato);
        return contrato;
    }

    @Override
    public Contrato findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Contrato> findAll() {
        return new ArrayList<>(storage.values());
    }
}