package dev.com.protactic.aplicacao.principal.clube;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Clubes.
 */
@Service // 1. Define como um "Bean" do Spring
public class ClubeServicoAplicacao {
    
    // 2. Depende da interface que acabámos de criar
    private final ClubeRepositorioAplicacao repositorio;

    // 3. O Spring vai injetar a implementação (que faremos na infra)
    public ClubeServicoAplicacao(ClubeRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todos os clubes (resumido)
     */
    public List<ClubeResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar clubes por competição (resumido)
     */
    public List<ClubeResumo> pesquisarResumosPorCompeticao(Integer competicaoId) {
        return repositorio.pesquisarResumosPorCompeticao(competicaoId);
    }
}