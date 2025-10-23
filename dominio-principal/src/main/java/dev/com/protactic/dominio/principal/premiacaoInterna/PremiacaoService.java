package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.*;
import java.util.*;

public class PremiacaoService {

    private final PremiacaoRepository repository;

    public PremiacaoService(PremiacaoRepository repository) {
        this.repository = repository;
    }

   
    public Premiacao criarPremiacaoMensal(String nomePremiacao, Date dataPremiacao, List<Jogador> jogadores) {
        Premiacao premiacao = repository.criarPremiacao(nomePremiacao, dataPremiacao);
        Jogador vencedor = definirVencedor(jogadores);

        if (vencedor == null) {
            System.out.println("DEBUG >> Nenhum vencedor definido para " + nomePremiacao);
            return null;
        }

        premiacao.setJogador(vencedor);
        repository.salvarPremiacao(premiacao);

        System.out.println("DEBUG >> Vencedor definitivo: " + vencedor.getNome());
        return premiacao;
    }

    private Jogador definirVencedor(List<Jogador> jogadores) {
        List<Jogador> candidatos = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (j.getNota() >= 6) {
                candidatos.add(j);
            }
        }

        if (candidatos.isEmpty()) return null;

        double maiorNota = candidatos.stream()
                .mapToDouble(Jogador::getNota)
                .max()
                .orElse(0);

        List<Jogador> empatados = new ArrayList<>();
        for (Jogador j : candidatos) {
            if (j.getNota() == maiorNota) {
                empatados.add(j);
            }
        }

        if (maiorNota == 6.0) {
            return empatados.get(0);
        } else if (empatados.size() > 1) {
            return empatados.stream()
                    .min(Comparator.comparingDouble(Jogador::getDesvioPadrao))
                    .orElse(null);
        } else {
            return empatados.get(0);
        }
    }

    
    public boolean verificarSeVencedorTemMenorDesvio(Jogador vencedor, List<Jogador> jogadores) {
        double menorDesvio = jogadores.stream()
                .mapToDouble(Jogador::getDesvioPadrao)
                .min()
                .orElse(Double.MAX_VALUE);
        return vencedor.getDesvioPadrao() == menorDesvio;
    }
}
