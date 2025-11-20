package dev.com.protactic.aplicacao.principal.escalacao;

import java.util.List;
import org.springframework.stereotype.Service;

@Service 
public class EscalacaoServicoAplicacao {
    
    private final EscalacaoRepositorioAplicacao repositorio;

    public EscalacaoServicoAplicacao(EscalacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData, Integer clubeId) {
        return repositorio.pesquisarResumosPorData(jogoData, clubeId);
    }
}