package dev.com.protactic.aplicacao.principal.contrato;

import java.util.List;
import org.springframework.stereotype.Service;

@Service 
public class ContratoServicoAplicacao {
    
    private final ContratoRepositorioAplicacao repositorio;

    public ContratoServicoAplicacao(ContratoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<ContratoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    public List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositorio.pesquisarResumosPorClube(clubeId);
    }
}