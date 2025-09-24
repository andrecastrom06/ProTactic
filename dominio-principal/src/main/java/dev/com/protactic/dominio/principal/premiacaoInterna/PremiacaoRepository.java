package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.Premiacao;
import java.util.*;

public class PremiacaoRepository implements IPremiacaoRepository {

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
