package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio.PremiacaoRepository;

import java.math.BigDecimal; // Importação necessária
import java.util.*;

public class PremiacaoMock implements PremiacaoRepository {

    private final List<Jogador> jogadores = new ArrayList<>();
    private final List<Premiacao> premiacoes = new ArrayList<>();

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        Premiacao nova = new Premiacao(0, null, nomePremiacao, dataPremiacao, BigDecimal.ZERO);
        return nova;
    }

    @Override
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