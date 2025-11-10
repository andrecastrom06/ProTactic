package dev.com.protactic.aplicacao.principal.inscricaoatleta;

import java.util.List;


public interface InscricaoAtletaRepositorioAplicacao {
   
    List<InscricaoAtletaResumo> pesquisarResumos();

    List<InscricaoAtletaResumo> pesquisarResumosPorAtleta(String atleta);

    List<InscricaoAtletaResumo> pesquisarResumosPorCompeticao(String competicao);
}