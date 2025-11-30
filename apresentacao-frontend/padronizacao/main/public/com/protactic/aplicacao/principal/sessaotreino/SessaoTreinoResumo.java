package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;


public interface SessaoTreinoResumo {
    
    Integer getId();
    String getNome();
    Integer getPartidaId();
    List<Integer> getConvocadosIds(); 

}