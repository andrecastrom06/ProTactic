package dev.com.protactic.aplicacao.principal.lesao;

import java.util.List;
import java.util.Optional; // Importar Optional
import org.springframework.stereotype.Service;

@Service 
public class LesaoServicoAplicacao {
    
   
    private final LesaoRepositorioAplicacao repositorio;

    public LesaoServicoAplicacao(LesaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

  
    public List<LesaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

   
    public List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }

 
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumoAtivoPorJogador(jogadorId);
    }
}