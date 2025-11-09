package dev.com.protactic.aplicacao.principal.inscricaoatleta;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Inscrição de Atletas.
 */
@Service // 1. Define como um "Bean" do Spring
public class InscricaoAtletaServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final InscricaoAtletaRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public InscricaoAtletaServicoAplicacao(InscricaoAtletaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as inscrições (resumido)
     */
    public List<InscricaoAtletaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar inscrições por atleta (resumido)
     */
    public List<InscricaoAtletaResumo> pesquisarResumosPorAtleta(String atleta) {
        return repositorio.pesquisarResumosPorAtleta(atleta);
    }

    /**
     * Caso de Uso: Listar inscrições por competição (resumido)
     */
    public List<InscricaoAtletaResumo> pesquisarResumosPorCompeticao(String competicao) {
        return repositorio.pesquisarResumosPorCompeticao(competicao);
    }
}