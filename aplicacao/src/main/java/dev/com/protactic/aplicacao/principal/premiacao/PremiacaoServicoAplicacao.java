package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.List;
import org.springframework.stereotype.Service;


@Service 
public class PremiacaoServicoAplicacao {
    
    private final PremiacaoRepositorioAplicacao repositorio;

    public PremiacaoServicoAplicacao(PremiacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

  
    public List<PremiacaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

   
    public List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }


    public List<PremiacaoResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.pesquisarResumosPorNome(nome);
    }
}