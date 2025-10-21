package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.*;
import java.util.*;

public class PremiacaoService {

    public Premiacao definirVencedor(String nomePremiacao, Date dataPremiacao, List<Jogador> jogadores) {
        Jogador vencedor = null;

        // Primeiro, filtra apenas os que têm nota >= 6
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

        // Encontra a maior nota
        double maiorNota = candidatos.stream().mapToDouble(Jogador::getNota).max().orElse(0);

        // Filtra apenas os jogadores com essa maior nota
        List<Jogador> empatados = new ArrayList<>();
        for (Jogador j : candidatos) {
            if (j.getNota() == maiorNota) {
                empatados.add(j);
            }
        }

        // Caso especial: se a maior média for exatamente 6.0
        if (maiorNota == 6.0) {
            vencedor = empatados.get(0); // qualquer com 6.0 pode ser o vencedor
        }
        // Se houve empate, decide pelo menor desvio padrão
        else if (empatados.size() > 1) {
            vencedor = empatados.stream()
                    .min(Comparator.comparingDouble(Jogador::getDesvioPadrao))
                    .orElse(null);
        } 
        // Caso normal: apenas um tem a maior nota
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
