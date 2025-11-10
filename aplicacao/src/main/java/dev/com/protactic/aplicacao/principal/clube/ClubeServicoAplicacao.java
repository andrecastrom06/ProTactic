package dev.com.protactic.aplicacao.principal.clube;

import java.util.List;
import org.springframework.stereotype.Service;

@Service // 1. Define como um "Bean" do Spring
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