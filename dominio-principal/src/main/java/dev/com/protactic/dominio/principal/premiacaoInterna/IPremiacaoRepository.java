package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.Premiacao;
import java.util.List;

public interface IPremiacaoRepository {
    void salvar(Premiacao premiacao);
    Premiacao buscarPorId(int id);
    List<Premiacao> listarTodos();
}
