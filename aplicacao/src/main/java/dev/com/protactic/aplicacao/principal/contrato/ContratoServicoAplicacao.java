package dev.com.protactic.aplicacao.principal.contrato;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Contratos.
 */
@Service // 1. Define como um "Bean" do Spring
public class ContratoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final ContratoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public ContratoServicoAplicacao(ContratoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todos os contratos (resumido)
     */
    public List<ContratoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar contratos por clube (resumido)
     */
    public List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositorio.pesquisarResumosPorClube(clubeId);
    }
}