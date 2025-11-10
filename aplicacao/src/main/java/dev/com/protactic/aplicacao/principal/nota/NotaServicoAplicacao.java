package dev.com.protactic.aplicacao.principal.nota;

import java.util.List;
import org.springframework.stereotype.Service;


@Service 
public class NotaServicoAplicacao {
    
    private final NotaRepositorioAplicacao repositorio;

    public NotaServicoAplicacao(NotaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    
    public List<NotaResumo> pesquisarResumosPorJogo(String jogoId) {
        return repositorio.pesquisarResumosPorJogo(jogoId);
    }

    public List<NotaResumo> pesquisarResumosPorJogador(String jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }
}