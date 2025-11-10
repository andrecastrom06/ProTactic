package dev.com.protactic.aplicacao.principal.nota;

import java.util.List;


public interface NotaRepositorioAplicacao {
    
  
    List<NotaResumo> pesquisarResumosPorJogo(String jogoId);

   
    List<NotaResumo> pesquisarResumosPorJogador(String jogadorId);
}