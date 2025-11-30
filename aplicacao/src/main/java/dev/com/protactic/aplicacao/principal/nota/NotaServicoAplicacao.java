package dev.com.protactic.aplicacao.principal.nota;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service 
public class NotaServicoAplicacao {
    
    
    private @Autowired NotaRepositorioAplicacao repositorio;

    public NotaServicoAplicacao(NotaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    
    public List<NotaResumo> pesquisarResumosPorJogo(String jogoId) {
        return repositorio.pesquisarResumosPorJogo(jogoId);
    }

    public List<NotaResumo> pesquisarResumosPorJogador(String jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }


    
    public Integer encontrarJogadorComMelhorNotaNoMes(Date data) {
        Optional<Integer> jogadorId = repositorio.encontrarJogadorComMelhorNotaNoMes(data);
        return jogadorId.orElse(null);
    }
}