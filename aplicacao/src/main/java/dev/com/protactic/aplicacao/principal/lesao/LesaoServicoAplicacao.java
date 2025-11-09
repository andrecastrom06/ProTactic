package dev.com.protactic.aplicacao.principal.lesao;

import java.util.List;
import java.util.Optional; // Importar Optional
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Lesoes.
 */
@Service // 1. Define como um "Bean" do Spring
public class LesaoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final LesaoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public LesaoServicoAplicacao(LesaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as lesões (resumido)
     */
    public List<LesaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar lesões por jogador (resumido)
     */
    public List<LesaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }

    /**
     * Caso de Uso: Buscar lesão ativa por jogador (resumido)
     */
    public Optional<LesaoResumo> pesquisarResumoAtivoPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumoAtivoPorJogador(jogadorId);
    }
}