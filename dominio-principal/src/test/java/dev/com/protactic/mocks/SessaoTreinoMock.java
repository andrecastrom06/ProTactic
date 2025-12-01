package dev.com.protactic.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade.SessaoTreino;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.repositorio.SessaoTreinoRepository;

public class SessaoTreinoMock implements SessaoTreinoRepository {
    private final List<SessaoTreino> banco = new ArrayList<>();
    private SessaoTreino ultimaSessaoSalva;

    @Override
    public void salvar(SessaoTreino sessao) {
        banco.add(sessao);
        this.ultimaSessaoSalva = sessao; 
    }

    @Override
    public List<SessaoTreino> listarPorPartida(String partidaNome) {
        return banco.stream()
            .filter(s -> s.getPartida().getDescricao().equalsIgnoreCase(partidaNome))
            .collect(Collectors.toList());
    }

    public SessaoTreino getUltimaSessaoSalva() {
        return ultimaSessaoSalva;
    }

    public void limpar() {
        banco.clear();
        ultimaSessaoSalva = null;
    }
}
