package dev.com.protactic.aplicacao.principal.registrocartao;

import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class RegistroCartaoServicoAplicacao {
    
    private final RegistroCartaoRepositorioAplicacao repositorio;

    public RegistroCartaoServicoAplicacao(RegistroCartaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }


    public List<RegistroCartaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

 
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta) {
        return repositorio.pesquisarResumosPorAtleta(atleta);
    }


    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo) {
        return repositorio.pesquisarResumosPorTipo(tipo);
    }
}