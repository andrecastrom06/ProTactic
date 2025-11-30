package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.List;


public interface PremiacaoRepositorioAplicacao {
    

    List<PremiacaoResumo> pesquisarResumos();


    List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId);

    List<PremiacaoResumo> pesquisarResumosPorNome(String nome);
}