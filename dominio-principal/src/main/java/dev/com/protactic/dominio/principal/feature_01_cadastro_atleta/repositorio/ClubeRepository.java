package dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;

public interface ClubeRepository {
    void salvar(Clube clube);
    Clube buscarPorNome(String nome);
    List<Clube> listarTodos();
    
    Clube buscarPorId(Integer id); 
}