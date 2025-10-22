package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Jogador;
import java.util.List;

public interface JogadorRepository {
    void salvar(Jogador jogador);
    Jogador buscarPorNome(String nome);
    boolean existe(String nome);
    List<Jogador> listarTodos();

    Jogador buscarPorId(Integer id);
}