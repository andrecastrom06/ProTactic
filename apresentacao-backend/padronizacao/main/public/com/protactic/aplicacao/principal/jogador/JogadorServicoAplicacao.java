package dev.com.protactic.aplicacao.principal.jogador;

import java.util.List;
import org.springframework.stereotype.Service;


@Service 
public class JogadorServicoAplicacao {
    
    private final JogadorRepositorioAplicacao repositorio;

    public JogadorServicoAplicacao(JogadorRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    
    public List<JogadorResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

  
    public List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositorio.pesquisarResumosPorClube(clubeId);
    }
}