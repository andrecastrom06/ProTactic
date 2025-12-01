package dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio;

import java.util.Optional;

import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Nota;

public interface NotaRepository {
    Optional<Nota> buscar(String jogoId, String jogadorId);
    void salvar(Nota nota);
    void registrarParticipacao(String jogoId, String jogadorId, boolean atuou);
    boolean atuouNoJogo(String jogoId, String jogadorId);
    void registrarJogadorNoElenco(String jogadorId);
    boolean jogadorExisteNoElenco(String jogadorId);
    void limpar();
}