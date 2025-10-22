package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Jogador;
import java.util.List;

public interface IJogadorRepository {
    void salvar(Jogador jogador);
    Jogador buscarPorNome(String nome);
    boolean existe(String nome);
    List<Jogador> listarTodos();

    /**
     * Busca um Jogador pelo seu ID.
     * @param id O ID do jogador.
     * @return O agregado Jogador encontrado ou null se não existir.
     */
    Jogador buscarPorId(Integer id);
}