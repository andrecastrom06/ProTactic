package dev.com.protactic.dominio.principal.feature_07_definir_capitao.repositorio;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.entidade.Capitao;

public interface CapitaoRepository {
    void salvarCapitao(Capitao capitao);
    Capitao buscarCapitaoPorClube(Integer clubeId);
}
