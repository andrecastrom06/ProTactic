package dev.com.protactic.aplicacao.principal.proposta;

import java.util.Date;

public interface PropostaResumo {
    
  
    int getId();
    String getStatus();
    double getValor();
    Date getData();
    Integer getJogadorId(); 

   
    
    
    String getAtletaNome();
    String getAtletaPosicao();
    int getAtletaIdade();


    String getClubeAtualNome();
}