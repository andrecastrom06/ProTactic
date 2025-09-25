package dev.com.protactic.dominio.principal.nota;

import java.util.*;

import dev.com.protactic.dominio.principal.Nota;

public class NotaMock implements NotaRepository {
    private final Map<String, Nota> notasPorChave = new HashMap<>();
    private final Map<String, Set<String>> atuaramPorJogo = new HashMap<>();
    private final Set<String> elenco = new HashSet<>();

    private static String chave(String jogoId, String jogadorId) {
        return jogoId + "|" + jogadorId;
    }

    @Override
    public Optional<Nota> buscar(String jogoId, String jogadorId) {
        return Optional.ofNullable(notasPorChave.get(chave(jogoId, jogadorId)));
    }

    @Override
    public void salvar(Nota nota) {
        notasPorChave.put(chave(nota.getJogoId(), nota.getJogadorId()), nota);
    }

    @Override
    public void registrarParticipacao(String jogoId, String jogadorId, boolean atuou) {
        Set<String> set = atuaramPorJogo.computeIfAbsent(jogoId, k -> new HashSet<>());
        if (atuou) {
            set.add(jogadorId);
        } else {
            set.remove(jogadorId);
        }
    }

    @Override
    public boolean atuouNoJogo(String jogoId, String jogadorId) {
        return atuaramPorJogo.getOrDefault(jogoId, Collections.emptySet()).contains(jogadorId);
    }

    @Override
    public void registrarJogadorNoElenco(String jogadorId) {
        elenco.add(jogadorId);
    }

    @Override
    public boolean jogadorExisteNoElenco(String jogadorId) {
        return elenco.contains(jogadorId);
    }

    @Override
    public void limpar() {
        notasPorChave.clear();
        atuaramPorJogo.clear();
        elenco.clear();
    }
}
