package dev.com.protactic.aplicacao.principal.escalacao;

import java.util.List;
import org.springframework.stereotype.Service;


@Service // 1. Define como um "Bean" do Spring
public class EscalacaoServicoAplicacao {
    
    private final EscalacaoRepositorioAplicacao repositorio;

    public EscalacaoServicoAplicacao(EscalacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData) {
        return repositorio.pesquisarResumosPorData(jogoData);
    }
}