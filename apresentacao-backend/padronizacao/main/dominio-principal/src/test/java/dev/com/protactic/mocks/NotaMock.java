package dev.com.protactic.mocks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import dev.com.protactic.dominio.principal.Nota;
import dev.com.protactic.dominio.principal.nota.NotaRepository;

public class NotaMock implements NotaRepository {

    private Nota ultimaNota;
    private final Map<String, Nota> store = new ConcurrentHashMap<>();
    private final Set<String> elenco = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<String>> participacoesPorJogo = new ConcurrentHashMap<>();

    private String key(String jogoId, String jogadorId) {
        return jogoId + "##" + jogadorId;
    }

    @Override
    public Optional<Nota> buscar(String jogoId, String jogadorId) {
        return Optional.ofNullable(store.get(key(jogoId, jogadorId)));
    }

    @Override
    public void salvar(Nota nota) {
        store.put(key(nota.getJogoId(), nota.getJogadorId()), nota);
        ultimaNota = nota;
    }

    public Nota getUltimaNota() {
        return ultimaNota;
    }

    @Override
    public void registrarParticipacao(String jogoId, String jogadorId, boolean atuou) {
        participacoesPorJogo.computeIfAbsent(jogoId, k -> ConcurrentHashMap.newKeySet());
        if (atuou) {
            participacoesPorJogo.get(jogoId).add(jogadorId);
            store.computeIfAbsent(key(jogoId, jogadorId), k -> new Nota(jogoId, jogadorId, null, null));
        } else {
            participacoesPorJogo.get(jogoId).remove(jogadorId);
        }
    }

    @Override
    public boolean atuouNoJogo(String jogoId, String jogadorId) {
        return participacoesPorJogo
                .getOrDefault(jogoId, Collections.emptySet())
                .contains(jogadorId);
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
        store.clear();
        elenco.clear();
        participacoesPorJogo.clear();
    }

    public Nota buscarPersistido(String jogoId, String jogadorId) {
        return store.get(key(jogoId, jogadorId));
    }
}