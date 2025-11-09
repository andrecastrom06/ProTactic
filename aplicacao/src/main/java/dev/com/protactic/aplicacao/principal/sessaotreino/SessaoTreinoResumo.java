package dev.com.protactic.aplicacao.principal.sessaotreino;

import java.util.List;

/**
 * Interface de Projeção (Resumo/DTO) para a entidade SessaoTreino.
 * Contém apenas os dados necessários para listagens.
 */
public interface SessaoTreinoResumo {
    
    Integer getId();
    String getNome();
    Integer getPartidaId();
    List<Integer> getConvocadosIds(); // A projeção pode incluir a lista de IDs

}