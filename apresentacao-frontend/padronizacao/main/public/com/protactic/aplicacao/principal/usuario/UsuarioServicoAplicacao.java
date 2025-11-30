package dev.com.protactic.aplicacao.principal.usuario;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicoAplicacao {
    
    private final UsuarioRepositorioAplicacao repositorio;

    public UsuarioServicoAplicacao(UsuarioRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    public List<UsuarioResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }
}