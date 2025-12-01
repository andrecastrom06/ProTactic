package dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;

public interface JogadorRepository {
    void salvar(Jogador jogador);
    Jogador buscarPorNome(String nome);
    boolean existe(String nome);
    List<Jogador> listarTodos();
    Jogador buscarPorId(Integer id);
    List<Jogador> findByNomeIgnoreCase(String nome);
    List<Jogador> buscarPorClube(Integer clubeId);
}