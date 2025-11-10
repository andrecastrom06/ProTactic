package dev.com.protactic.aplicacao.principal.registrocartao;

import java.util.List;


public interface RegistroCartaoRepositorioAplicacao {
    

    List<RegistroCartaoResumo> pesquisarResumos();

    List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta);

    List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo);
}