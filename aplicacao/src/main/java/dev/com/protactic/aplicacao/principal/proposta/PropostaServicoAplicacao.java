package dev.com.protactic.aplicacao.principal.proposta;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Serviço de Aplicação (Casos de Uso) para consultas
 * relacionadas a Propostas.
 */
@Service // 1. Define como um "Bean" do Spring
public class PropostaServicoAplicacao {
    
    // 2. Depende da interface da aplicação
    private final PropostaRepositorioAplicacao repositorio;

    // 3. O Spring injeta a implementação da infraestrutura
    public PropostaServicoAplicacao(PropostaRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Caso de Uso: Listar todas as propostas (resumido)
     */
    public List<PropostaResumo> pesquisarResumos() {
        return repositorio.pesquisarResumos();
    }

    /**
     * Caso de Uso: Listar propostas por Propositor (resumido)
     */
    public List<PropostaResumo> pesquisarResumosPorPropositor(Integer propositorId) {
        return repositorio.pesquisarResumosPorPropositor(propositorId);
    }

    /**
     * Caso de Uso: Listar propostas por Receptor (resumido)
     */
    public List<PropostaResumo> pesquisarResumosPorReceptor(Integer receptorId) {
        return repositorio.pesquisarResumosPorReceptor(receptorId);
    }

    /**
     * Caso de Uso: Listar propostas por Jogador (resumido)
     */
    public List<PropostaResumo> pesquisarResumosPorJogador(Integer jogadorId) {
        return repositorio.pesquisarResumosPorJogador(jogadorId);
    }
}