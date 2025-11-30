package dev.com.protactic.aplicacao.principal.fisico;

import java.util.List;

public interface FisicoRepositorioAplicacao {

    List<FisicoResumo> pesquisarResumosPorJogador(Integer jogadorId);

}