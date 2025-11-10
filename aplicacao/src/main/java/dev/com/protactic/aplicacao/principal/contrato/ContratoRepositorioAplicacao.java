package dev.com.protactic.aplicacao.principal.contrato;

import java.util.List;

public interface ContratoRepositorioAplicacao {
    
 
    List<ContratoResumo> pesquisarResumos();
    
    List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId);
}