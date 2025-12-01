package dev.com.protactic.dominio.principal.feature_10_treino_tatico.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade.SessaoTreino;

public interface SessaoTreinoRepository {
    void salvar(SessaoTreino sessao);
    List<SessaoTreino> listarPorPartida(String partidaNome);
}
