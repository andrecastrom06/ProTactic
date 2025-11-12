package dev.com.protactic.aplicacao.principal.fisico;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FisicoServicoAplicacao {

    private final FisicoRepositorioAplicacao repositorio;

    public FisicoServicoAplicacao(FisicoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<FisicoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }
}