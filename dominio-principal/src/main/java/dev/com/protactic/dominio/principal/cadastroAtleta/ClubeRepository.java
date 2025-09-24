package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Clube;
import java.util.*;

public class ClubeRepository {
    private final Map<String, Clube> clubes = new HashMap<>();

    public void salvar(Clube clube) {
        clubes.put(clube.getNome(), clube);
    }

    public Clube buscarPorNome(String nome) {
        return clubes.get(nome);
    }

    public List<Clube> listarTodos() {
        return new ArrayList<>(clubes.values());
    }
}
