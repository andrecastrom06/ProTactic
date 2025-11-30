package dev.com.protactic.aplicacao.principal.inscricaoatleta;

import java.util.List;
import org.springframework.stereotype.Service;

@Service 
public class InscricaoAtletaServicoAplicacao {
    
    private final InscricaoAtletaRepositorioAplicacao repositorio;

    public InscricaoAtletaServicoAplicacao(InscricaoAtletaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    
    public List<InscricaoAtletaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    public List<InscricaoAtletaResumo> pesquisarResumosPorAtleta(String atleta) {
        return repositorio.pesquisarResumosPorAtleta(atleta);
    }

   
    public List<InscricaoAtletaResumo> pesquisarResumosPorCompeticao(String competicao) {
        return repositorio.pesquisarResumosPorCompeticao(competicao);
    }
}