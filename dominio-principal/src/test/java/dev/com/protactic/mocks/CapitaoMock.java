package dev.com.protactic.mocks;


import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

import java.util.HashMap;
import java.util.Map;
import dev.com.protactic.dominio.principal.Capitao;



public class CapitaoMock implements CapitaoRepository {

    private final Map<Integer, Capitao> capitoes = new HashMap<>();
    private Capitao ultimoCapitaoSalvo;

    @Override
    public void salvarCapitao(Capitao capitao) {
        if (capitao.getClubeId() != null) {
            capitoes.put(capitao.getClubeId(), capitao);
            this.ultimoCapitaoSalvo = capitao;
        }
    }

    @Override
    public Capitao buscarCapitaoPorClube(Integer clubeId) {
        return capitoes.get(clubeId);
    }

    public Capitao getUltimoCapitaoSalvo() {
        return ultimoCapitaoSalvo;
    }

    public void limpar() {
        capitoes.clear();
        ultimoCapitaoSalvo = null;
    }
}
