package dev.com.protactic.aplicacao.principal.jogador;

import java.util.List;


public interface JogadorRepositorioAplicacao {
    
    List<JogadorResumo> pesquisarResumos();

    List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId);
}