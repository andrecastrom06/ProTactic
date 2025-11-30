package dev.com.protactic.aplicacao.principal.fisico;

import java.util.Date;

public interface FisicoResumo {
    int getId();
    Integer getJogadorId();
    String getNome();
    String getMusculo();
    String getIntensidade();
    String getDescricao();
    Date getDataInicio();
    Date getDataFim();
    String getStatus();
}