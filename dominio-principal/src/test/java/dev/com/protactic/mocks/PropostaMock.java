package dev.com.protactic.mocks;

import java.util.*;

import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Proposta;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.PropostaRepository;

public class PropostaMock implements PropostaRepository {
    private Proposta ultimaProposta;
    private Map<Long, Proposta> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public Proposta saveProposta(Proposta proposta) {
        if (proposta.getId() == 0) { // Garante que novas propostas também recebam ID
            proposta.setId((int) sequence++);
        }
        storage.put((long) proposta.getId(), proposta);
        this.ultimaProposta = proposta;
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

    // --- (INÍCIO DA CORREÇÃO) ---
    // Adiciona o método em falta da interface
    @Override
    public void deleteProposta(Proposta proposta) {
        storage.remove((long) proposta.getId());
    }
    // --- (FIM DA CORREÇÃO) ---

    public Proposta getUltimaProposta() {
        return ultimaProposta;
    }
}