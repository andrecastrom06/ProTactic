package dev.com.protactic.dominio.principal.nota;

import dev.com.protactic.dominio.principal.Nota;
import java.util.Optional;

public interface NotaRepository {
    Optional<Nota> buscar(String jogoId, String jogadorId);
    void salvar(Nota nota);
    void registrarParticipacao(String jogoId, String jogadorId, boolean atuou);
    boolean atuouNoJogo(String jogoId, String jogadorId);
    void registrarJogadorNoElenco(String jogadorId);
    boolean jogadorExisteNoElenco(String jogadorId);
    void limpar();
}