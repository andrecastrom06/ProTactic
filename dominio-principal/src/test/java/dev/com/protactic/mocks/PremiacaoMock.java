package dev.com.protactic.mocks;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import dev.com.protactic.dominio.principal.premiacaoInterna.IPremiacaoRepository;
import dev.com.protactic.dominio.principal.premiacaoInterna.PremiacaoService;

import java.util.*;

public class PremiacaoMock implements IPremiacaoRepository {
    private final List<Jogador> jogadores = new ArrayList<>();

    public PremiacaoMock() {
        // Por padrão, cria alguns jogadores de exemplo
        jogadores.add(new Jogador("Carlos"));
        jogadores.get(0).setNota(7.5);

        jogadores.add(new Jogador("Pedro"));
        jogadores.get(1).setNota(5.9);

        jogadores.add(new Jogador("Lucas"));
        jogadores.get(2).setNota(8.2);
    }

    @Override
    public Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao) {
        PremiacaoService service = new PremiacaoService();
        return service.definirVencedor(nomePremiacao, dataPremiacao, jogadores);
    }

    // Permite manipular jogadores nos testes
    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void clearJogadores() {
        jogadores.clear();
    }

    public void addJogador(String nome, double nota) {
        Jogador j = new Jogador(nome);
        j.setNota(nota);
        jogadores.add(j);
    }
}
