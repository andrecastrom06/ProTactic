package dev.com.protactic.dominio.principal.treinoTatico;

import java.util.List;

public interface SessaoTreinoRepository {
    void salvar(SessaoTreino sessao);
    List<SessaoTreino> listarPorPartida(String partidaNome);
}
