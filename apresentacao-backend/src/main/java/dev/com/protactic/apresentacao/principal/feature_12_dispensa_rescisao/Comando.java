package dev.com.protactic.apresentacao.principal.feature_12_dispensa_rescisao;

/**
 * Interface Command (Comando) do padrão Command.
 * Encapsula uma solicitação como um objeto.
 * @param <T> O tipo de retorno do comando.
 */
public interface Comando<T> {
    T executar() throws Exception; // <-- ADICIONADO: throws Exception
}