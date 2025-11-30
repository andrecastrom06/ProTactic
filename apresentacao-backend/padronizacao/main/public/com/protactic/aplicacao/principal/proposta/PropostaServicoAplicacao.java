package dev.com.protactic.aplicacao.principal.proposta;

import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class PropostaServicoAplicacao {
    
    private final PropostaRepositorioAplicacao repositorio;

    public PropostaServicoAplicacao(PropostaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

 
    public List<PropostaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

  
    public List<PropostaResumo> pesquisarResumosPorPropositor(Integer propositorId) {
        return repositorio.pesquisarResumosPorPropositor(propositorId);
    }

    public List<PropostaResumo> pesquisarResumosPorReceptor(Integer receptorId) {
        return repositorio.pesquisarResumosPorReceptor(receptorId);
    }

 
    public List<PropostaResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }
}