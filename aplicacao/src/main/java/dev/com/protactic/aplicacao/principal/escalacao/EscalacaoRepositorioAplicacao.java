package dev.com.protactic.aplicacao.principal.escalacao;

import java.util.List;


public interface EscalacaoRepositorioAplicacao {
    
    List<EscalacaoResumo> pesquisarResumosPorData(String jogoData);
}