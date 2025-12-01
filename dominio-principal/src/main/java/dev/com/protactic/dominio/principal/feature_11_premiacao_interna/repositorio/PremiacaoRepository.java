package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio;

import java.util.Date;

import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;

public interface PremiacaoRepository {
    Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao);
    void salvarPremiacao(Premiacao premiacao);
}
