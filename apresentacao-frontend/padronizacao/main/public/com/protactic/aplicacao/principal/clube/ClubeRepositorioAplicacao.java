package dev.com.protactic.aplicacao.principal.clube;

import java.util.List;


public interface ClubeRepositorioAplicacao {
    
    
    List<ClubeResumo> pesquisarResumos();

    
    List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId);
}