package dev.com.protactic.aplicacao.principal.nota;

import java.util.List;
import java.util.Date;
import java.util.Optional;

public interface NotaRepositorioAplicacao {
    
  
    List<NotaResumo> pesquisarResumosPorJogo(String jogoId);

   
    List<NotaResumo> pesquisarResumosPorJogador(String jogadorId);

    Optional<Integer> encontrarJogadorComMelhorNotaNoMes(Date data); // NOVO MÉTODO
}