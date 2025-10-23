package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoRepository;

import java.util.*;

public class PremiacaoMock implements PremiacaoRepository {

    private final List<Jogador> jogadores = new ArrayList<>();
    private final List<Premiacao> premiacoes = new ArrayList<>();

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        Premiacao nova = new Premiacao(0, null, nomePremiacao, dataPremiacao);
        premiacoes.add(nova);
        return nova;
    }

    public void salvarPremiacao(Premiacao premiacao) {
        premiacoes.add(premiacao);
    }

    public List<Premiacao> getPremiacoes() {
        return premiacoes;
    }

    public Premiacao getUltimaPremiacao() {
        if (premiacoes.isEmpty()) return null;
        return premiacoes.get(premiacoes.size() - 1);
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void clear() {
        jogadores.clear();
        premiacoes.clear();
    }

    public void registrarJogador(String nome, String notaStr) {
        double nota = Double.parseDouble(notaStr.replace(",", "."));
        Jogador j = new Jogador(nome);
        j.setNota(nota);
        jogadores.add(j);
    }
}
