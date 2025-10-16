package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.*;
import java.util.Date;
import java.util.List;

public class PremiacaoService {

    public Premiacao definirVencedor(String nomePremiacao, Date dataPremiacao, List<Jogador> jogadores) {
        Jogador vencedor = null;

        for (Jogador j : jogadores) {
            if (j.getNota() >= 6) { // regra da nota mínima
                if (vencedor == null || j.getNota() > vencedor.getNota()) {
                    vencedor = j;
                }
            }
        }

        if (vencedor == null) {
            System.out.println("DEBUG >> Nenhum vencedor definido para " + nomePremiacao);
            return null; // nenhum vencedor
        }

        System.out.println("DEBUG >> Vencedor provisório: " + vencedor.getNome() + " (nota " + vencedor.getNota() + ")");
        return new Premiacao(1, vencedor, nomePremiacao, dataPremiacao);
    }
}
