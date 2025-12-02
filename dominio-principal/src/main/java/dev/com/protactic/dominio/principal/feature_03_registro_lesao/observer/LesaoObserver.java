package dev.com.protactic.dominio.principal.feature_03_registro_lesao.observer;

import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;

/**
 * Interface Observer (Assinante).
 * Define o contrato para qualquer serviço que deseja ser notificado
 * quando uma nova Lesao for registrada.
 */
public interface LesaoObserver {
    
    /**
     * Método de notificação (Update no diagrama do Observer).
     *
     * @param lesao A entidade Lesao recém-registrada.
     */
    void observarLesao(Lesao lesao);
}