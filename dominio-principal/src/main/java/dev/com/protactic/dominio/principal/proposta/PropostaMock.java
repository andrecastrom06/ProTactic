package dev.com.protactic.dominio.principal.proposta;

import java.util.*;

import dev.com.protactic.dominio.principal.Proposta;

public class PropostaMock implements PropostaRepository {
    private Map<Long, Proposta> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public Proposta saveProposta(Proposta proposta) {
        proposta.setId((int) sequence++);
        storage.put((long) proposta.getId(), proposta);
        return proposta;
    }

    @Override
    public Proposta findPropostaById(int id) {
        return storage.get((long) id);
    }

    @Override
    public List<Proposta> findAllPropostas() {
        return new ArrayList<>(storage.values());
    }
}
