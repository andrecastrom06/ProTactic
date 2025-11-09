package dev.com.protactic.aplicacao.principal.registrocartao;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Registro de Cartões.
 */
@Service // 1. Define como um "Bean" do Spring
public class RegistroCartaoServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final RegistroCartaoRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public RegistroCartaoServicoAplicacao(RegistroCartaoRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todos os registros (resumido)
     */
    public List<RegistroCartaoResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar registros por atleta (resumido)
     */
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta) {
        return repositorio.pesquisarResumosPorAtleta(atleta);
    }

    /**
     * Caso de Uso: Listar registros por tipo (resumido)
     */
    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo) {
        return repositorio.pesquisarResumosPorTipo(tipo);
    }
}