package dev.com.protactic.aplicacao.principal.competicao;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompeticaoServicoAplicacao {
    
    private final CompeticaoRepositorioAplicacao repositorio;

    public CompeticaoServicoAplicacao(CompeticaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<CompeticaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }
}