package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;

public interface SessaoTreinoRepositorioAplicacao {
    
   
    List<SessaoTreinoResumo> pesquisarResumos();

    List<SessaoTreinoResumo> pesquisarResumosPorPartida(Integer partidaId);

    List<SessaoTreinoResumo> pesquisarResumosPorConvocado(Integer jogadorId);
}