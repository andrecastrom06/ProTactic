package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.Clube;
import java.util.List;

public interface IClubeRepository {
    void salvar(Clube clube);
    Clube buscarPorNome(String nome);
    List<Clube> listarTodos();
}
