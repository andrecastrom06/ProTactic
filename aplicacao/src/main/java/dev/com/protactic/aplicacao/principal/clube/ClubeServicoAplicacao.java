package dev.com.protactic.aplicacao.principal.clube;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClubeServicoAplicacao {
    
    private final ClubeRepositorioAplicacao repositorio;

    public ClubeServicoAplicacao(ClubeRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<ClubeResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    public List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId) {
        return repositorio.pesquisarResumosPorCompeticao(competicaoId);
    }
}