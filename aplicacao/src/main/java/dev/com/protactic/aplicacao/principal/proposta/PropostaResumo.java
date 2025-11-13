package dev.com.protactic.aplicacao.principal.proposta;

import java.util.Date;

public interface PropostaResumo {
    
    // Campos da Proposta
    int getId();
    String getStatus();
    double getValor();
    Date getData();
    Integer getJogadorId(); // Mantemos o ID, é útil

    // --- NOVOS CAMPOS (do JOIN) ---
    
    // Campos do Jogador (j)
    String getAtletaNome();
    String getAtletaPosicao();
    int getAtletaIdade();

    // Campos do Clube (c) - O clube atual do jogador
    String getClubeAtualNome();
}