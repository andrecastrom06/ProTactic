package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Clube;
import java.util.*;

public class ClubeRepository implements IClubeRepository {
    private final Map<String, Clube> clubes = new HashMap<>();

    @Override
    public void salvar(Clube clube) {
        clubes.put(clube.getNome(), clube);
    }

    @Override
    public Clube buscarPorNome(String nome) {
        return clubes.get(nome);
    }

    @Override
    public List<Clube> listarTodos() {
        return new ArrayList<>(clubes.values());
    }
}
