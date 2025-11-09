package dev.com.protactic.aplicacao.principal.nota;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Notas.
 */
@Service // 1. Define como um "Bean" do Spring
public class NotaServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final NotaRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public NotaServicoAplicacao(NotaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar notas por Jogo (resumido)
     */
    public List<NotaResumo> pesquisarResumosPorJogo(String jogoId) {
        return repositorio.pesquisarResumosPorJogo(jogoId);
    }

    /**
     * Caso de Uso: Listar notas por Jogador (resumido)
     */
    public List<NotaResumo> pesquisarResumosPorJogador(String jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }
}