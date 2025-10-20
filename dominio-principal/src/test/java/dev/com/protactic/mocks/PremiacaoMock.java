package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.IPremiacaoRepository;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;

import java.util.*;

public class PremiacaoMock implements IPremiacaoRepository {
    private final List<Jogador> jogadores = new ArrayList<>();
    private final List<Premiacao> premiacoes = new ArrayList<>(); // <- armazena premiações criadas

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        PremiacaoService service = new PremiacaoService();
        Premiacao nova = service.definirVencedor(nomePremiacao, dataPremiacao, jogadores);
        if (nova != null) {
            premiacoes.add(nova); // persiste no "mock"
        }
        return nova;
    }

    // --- Métodos auxiliares para os testes ---
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
}
