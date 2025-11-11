package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import dev.com.protactic.dominio.principal.Suspensao;
import java.util.Optional;

/**
 * Interface de domínio para persistir o ESTADO de suspensão.
 */
public interface SuspensaoRepository {

    /**
     * Salva ou atualiza o estado de suspensão de um atleta.
     * @param suspensao O objeto de domínio Suspensao.
     */
    void salvarOuAtualizar(Suspensao suspensao);

    /**
     * Busca o estado de suspensão atual de um atleta pelo nome.
     * @param atleta O nome do atleta.
     * @return Um Optional contendo o estado da Suspensao, se existir.
     */
    Optional<Suspensao> buscarPorAtleta(String atleta);
}