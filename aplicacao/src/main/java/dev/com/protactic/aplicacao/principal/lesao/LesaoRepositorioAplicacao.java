package dev.com.protactic.aplicacao.principal.lesao;

import java.util.List;
import java.util.Optional; 


public interface LesaoRepositorioAplicacao {
    
  
    List<LesaoResumo> pesquisarResumos();

  
    List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId);

   
    Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId);
}