package dev.com.protactic.aplicacao.principal.contrato;

import java.time.LocalDate;

public interface ContratoResumo {
    
    int getId();
    int getDuracaoMeses();
    double getSalario();
    String getStatus();
    Integer getClubeId(); 
    String getAtletaNome();
    String getAtletaPosicao();
    int getAtletaIdade();
    LocalDate getDataInicio();

}