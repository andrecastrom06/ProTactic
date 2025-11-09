package dev.com.protactic.aplicacao.principal.jogador;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Jogadores.
 */
@Service // 1. Define como um "Bean" do Spring
public class JogadorServicoAplicacao {
    
    // 2. Depende da interface que acabámos de criar
    private final JogadorRepositorioAplicacao repositorio;

    // 3. O Spring vai injetar a implementação (que faremos na infra)
    public JogadorServicoAplicacao(JogadorRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todos os jogadores (resumido)
     */
    public List<JogadorResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar jogadores por clube (resumido)
     */
    public List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositorio.pesquisarResumosPorClube(clubeId);
    }
}