package dev.com.protactic.dominio.principal.treinoTatico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SessaoTreinoRepositoryFake implements SessaoTreinoRepository {
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
