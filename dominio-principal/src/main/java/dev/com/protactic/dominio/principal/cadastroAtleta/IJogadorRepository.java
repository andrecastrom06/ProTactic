package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Jogador;
import java.util.List;

public interface IJogadorRepository {
    void salvar(Jogador jogador);
    Jogador buscarPorNome(String nome);
    boolean existe(String nome);
    List<Jogador> listarTodos();
}
