package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.*;
import java.util.Date;

public class PremiacaoService {

    private final IPremiacaoRepository premiacaoRepo;

    public PremiacaoService(IPremiacaoRepository premiacaoRepo) {
        this.premiacaoRepo = premiacaoRepo;
    }

    public Premiacao criarPremiacaoMelhorJogador(Jogador jogadorMaiorPontuacao) {
        if (jogadorMaiorPontuacao != null && jogadorMaiorPontuacao.getNota() >= 6.0) {
            Premiacao premiacao = new Premiacao(
                1,
                jogadorMaiorPontuacao,
                "Melhor Jogador do Time",
                new Date()
            );
            premiacaoRepo.salvar(premiacao);
            return premiacao;
        }
        return null;
    }
}
