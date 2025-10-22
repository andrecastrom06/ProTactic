package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoRepository;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;

import java.util.*;

public class PremiacaoMock implements PremiacaoRepository {
    private final List<Jogador> jogadores = new ArrayList<>();
    private final List<Premiacao> premiacoes = new ArrayList<>();

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        PremiacaoService service = new PremiacaoService();
        Premiacao nova = service.definirVencedor(nomePremiacao, dataPremiacao, jogadores);
        if (nova != null) {
            premiacoes.add(nova);
        }
        return nova;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void clearJogadores() {
        jogadores.clear();
        premiacoes.clear();
    }

    public void addJogador(String nome, double nota) {
        Jogador j = new Jogador(nome);
        j.setNota(nota);
        jogadores.add(j);
    }

    public List<Premiacao> getPremiacoes() {
        return premiacoes;
    }

    public Premiacao getUltimaPremiacao() {
        if (premiacoes.isEmpty()) return null;
        return premiacoes.get(premiacoes.size() - 1);
    }

    public void registrarJogador(String nome, String notaStr) {
    double nota = Double.parseDouble(notaStr.replace(",", "."));
    addJogador(nome, nota);
}

}
