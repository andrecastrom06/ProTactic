package dev.com.protactic.dominio.principal.premiacaoInterna;


import dev.com.protactic.dominio.principal.Premiacao;
import java.util.*;

public class PremiacaoRepository {
    private final Map<Integer, Premiacao> premios = new HashMap<>();

    public void salvar(Premiacao premiacao) {
        premios.put(premiacao.getId(), premiacao);
    }

    public Premiacao buscarPorId(int id) {
        return premios.get(id);
    }

    public List<Premiacao> listarTodos() {
        return new ArrayList<>(premios.values());
    }
}

