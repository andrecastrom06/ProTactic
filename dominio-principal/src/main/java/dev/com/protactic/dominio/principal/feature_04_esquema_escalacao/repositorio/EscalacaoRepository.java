package dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.repositorio;

import java.util.List;

public interface EscalacaoRepository {
    void salvarJogadorNaEscalacao(String jogoData, String nomeJogador, Integer clubeId);
    
    List<String> obterEscalacaoPorData(String jogoData, Integer clubeId);
}