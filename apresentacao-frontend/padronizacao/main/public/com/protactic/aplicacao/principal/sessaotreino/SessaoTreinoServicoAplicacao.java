package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SessaoTreinoServicoAplicacao {
    
    private final SessaoTreinoRepositorioAplicacao repositorio;

    public SessaoTreinoServicoAplicacao(SessaoTreinoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<SessaoTreinoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    public List<SessaoTreinoResumo> pesquisarResumosPorPartida(Integer partidaId) {
        return repositorio.pesquisarResumosPorPartida(partidaId);
    }


    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(Integer jogadorId) {
        return repositorio.pesquisarResumosPorConvocado(jogadorId);
    }
}