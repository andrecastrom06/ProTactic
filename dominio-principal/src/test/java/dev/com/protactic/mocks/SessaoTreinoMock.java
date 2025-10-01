package dev.com.protactic.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreino;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoRepository;

public class SessaoTreinoMock implements SessaoTreinoRepository {
    private final List<SessaoTreino> banco = new ArrayList<>();

    @Override
    public void salvar(SessaoTreino sessao) {
        banco.add(sessao);
    }

    @Override
    public List<SessaoTreino> listarPorPartida(String partidaNome) {
        return banco.stream()
            .filter(s -> s.getPartida().getDescricao().equalsIgnoreCase(partidaNome))
            .collect(Collectors.toList());
    }
}
