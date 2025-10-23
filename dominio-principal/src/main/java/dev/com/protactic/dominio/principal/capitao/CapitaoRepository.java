package dev.com.protactic.dominio.principal.capitao;
import dev.com.protactic.dominio.principal.Capitao;

public interface CapitaoRepository {
    void salvarCapitao(Capitao capitao);
    Capitao buscarCapitaoPorClube(Integer clubeId);
}
