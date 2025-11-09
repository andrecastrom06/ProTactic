package dev.com.protactic.aplicacao.principal.premiacao;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Premiações.
 */
@Service // 1. Define como um "Bean" do Spring
public class PremiacaoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final PremiacaoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public PremiacaoServicoAplicacao(PremiacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as premiações (resumido)
     */
    public List<PremiacaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar premiações por jogador (resumido)
     */
    public List<PremiacaoResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }

    /**
     * Caso de Uso: Listar premiações por nome (resumido)
     */
    public List<PremiacaoResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.pesquisarResumosPorNome(nome);
    }
}