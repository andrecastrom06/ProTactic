package dev.com.protactic.aplicacao.principal.fisico;

import java.util.Date;

// Esta é a interface de Aplicação (para consultas)
public interface FisicoResumo {
    int getId();
    Integer getJogadorId();
    String getNome();
    String getMusculo();
    String getIntensidade();
    String getDescricao();
    Date getDataInicio();
    Date getDataFim();
}