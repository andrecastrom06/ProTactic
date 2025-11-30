package dev.com.protactic.dominio.principal.nota;

import java.math.BigDecimal;
import java.util.Optional;

import dev.com.protactic.dominio.principal.Nota;

public class NotaService {
    private final NotaRepository repo;

    public NotaService(NotaRepository repo) {
        this.repo = repo;
    }

    public void atribuirNotaEObservacao(String jogoId, String jogadorId, BigDecimal nota, String observacao) {
        if (!repo.atuouNoJogo(jogoId, jogadorId)) {
            throw new IllegalArgumentException("Apenas jogadores que atuaram podem receber nota técnica");
        }

        if (nota == null || nota.compareTo(BigDecimal.ZERO) < 0 || nota.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("Nota inválida. Informe um valor entre 0 e 10");
        }

        repo.salvar(new Nota(jogoId, jogadorId, nota, observacao));
    }

    public void registrarObservacao(String jogoId, String jogadorId, String observacao) {
        repo.salvar(new Nota(jogoId, jogadorId, null, observacao));
    }

    public Optional<Nota> obterRegistro(String jogoId, String jogadorId) {
        return repo.buscar(jogoId, jogadorId);
    }

    public void registrarJogadorNoElenco(String jogadorId) {
        repo.registrarJogadorNoElenco(jogadorId);
    }

    public void registrarParticipacao(String jogoId, String jogadorId, boolean atuou) {
        repo.registrarParticipacao(jogoId, jogadorId, atuou);
    }
}
