package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.IPremiacaoRepository;

import java.util.*;

public class PremiacaoMock implements IPremiacaoRepository {

    private final Map<Integer, Premiacao> premios = new HashMap<>();

    @Override
    public void salvar(Premiacao premiacao) {
        premios.put(premiacao.getId(), premiacao);
    }

    @Override
    public Premiacao buscarPorId(int id) {
        return premios.get(id);
    }

    @Override
    public List<Premiacao> listarTodos() {
        return new ArrayList<>(premios.values());
    }
}
