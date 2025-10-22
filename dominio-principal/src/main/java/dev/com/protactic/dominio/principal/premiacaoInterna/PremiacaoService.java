package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.*;
import java.util.*;

public class PremiacaoService {

    public Premiacao definirVencedor(String nomePremiacao, Date dataPremiacao, List<Jogador> jogadores) {
        Jogador vencedor = null;

        List<Jogador> candidatos = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (j.getNota() >= 6) {
                candidatos.add(j);
            }
        }

        if (candidatos.isEmpty()) {
            System.out.println("DEBUG >> Nenhum vencedor definido para " + nomePremiacao);
            return null;
        }

        double maiorNota = candidatos.stream().mapToDouble(Jogador::getNota).max().orElse(0);

        List<Jogador> empatados = new ArrayList<>();
        for (Jogador j : candidatos) {
            if (j.getNota() == maiorNota) {
                empatados.add(j);
            }
        }

        if (maiorNota == 6.0) {
            vencedor = empatados.get(0);
        }
        else if (empatados.size() > 1) {
            vencedor = empatados.stream()
                    .min(Comparator.comparingDouble(Jogador::getDesvioPadrao))
                    .orElse(null);
        } 
        else {
            vencedor = empatados.get(0);
        }

        if (vencedor == null) {
            System.out.println("DEBUG >> Nenhum vencedor definido para " + nomePremiacao);
            return null;
        }

        System.out.println("DEBUG >> Vencedor definitivo: " + vencedor.getNome() + " (nota " + vencedor.getNota() + ")");
        return new Premiacao(1, vencedor, nomePremiacao, dataPremiacao);
    }

    public boolean verificarSeVencedorTemMenorDesvio(Jogador vencedor, List<Jogador> jogadores) {
    double menorDesvio = jogadores.stream()
            .mapToDouble(Jogador::getDesvioPadrao)
            .min()
            .orElse(Double.MAX_VALUE);
    return vencedor.getDesvioPadrao() == menorDesvio;
}

}
