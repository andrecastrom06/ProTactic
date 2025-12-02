package dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.iterator;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;

public interface JogadorIterator {
    Jogador proximo(); 
    boolean existeProximo(); 
    void resetar();
}