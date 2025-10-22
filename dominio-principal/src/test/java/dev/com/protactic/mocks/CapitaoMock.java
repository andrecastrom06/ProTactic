package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;

import java.util.HashMap;
import java.util.Map;

public class CapitaoMock implements CapitaoRepository {

    private final Map<Integer, Jogador> capitoes = new HashMap<>();
    private Jogador ultimoCapitaoSalvo;

    @Override
    public void salvarCapitao(Jogador jogador) {
        if (jogador.getClubeId() != null) {
            capitoes.put(jogador.getClubeId(), jogador);
            this.ultimoCapitaoSalvo = jogador;
        }
    }

    
    @Override
    public Jogador buscarCapitaoPorClube(Integer clubeId) {
        return capitoes.get(clubeId);
    }
    
   
    /*
    @Override
    public Jogador buscarCapitaoPorClube(String clube) {
        System.err.println("AVISO: buscarCapitaoPorClube(String) não é suportado com o novo design de ID.");
        return null;
    }
    */

    public Jogador getUltimoCapitaoSalvo() { 
        return ultimoCapitaoSalvo;
    }

    public void limpar() {
        capitoes.clear();
        ultimoCapitaoSalvo = null; 
    }
}