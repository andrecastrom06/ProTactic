package dev.com.protactic.aplicacao.principal.proposta;

import java.util.List;

public interface PropostaRepositorioAplicacao {
    
    List<PropostaResumo> pesquisarResumos();

    List<PropostaResumo> pesquisarResumosPorPropositor(Integer propositorId);

    List<PropostaResumo> pesquisarResumosPorReceptor(Integer receptorId);

    List<PropostaResumo> pesquisarResumosPorJogador(Integer jogadorId);
}