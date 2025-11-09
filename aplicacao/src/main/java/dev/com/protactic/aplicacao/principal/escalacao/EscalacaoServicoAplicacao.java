package dev.com.protactic.aplicacao.principal.escalacao;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Escalacao.
 */
@Service // 1. Define como um "Bean" do Spring
public class EscalacaoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final EscalacaoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public EscalacaoServicoAplicacao(EscalacaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar escalação por data (resumido)
     */
    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData) {
        return repositorio.pesquisarResumosPorData(jogoData);
    }
}