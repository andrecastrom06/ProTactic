package dev.com.protactic.dominio.principal.treinoTatico;

import java.util.List;

import dev.com.protactic.dominio.principal.SessaoTreino;

public interface SessaoTreinoRepository {
    void salvar(SessaoTreino sessao);
    List<SessaoTreino> listarPorPartida(String partidaNome);
}
