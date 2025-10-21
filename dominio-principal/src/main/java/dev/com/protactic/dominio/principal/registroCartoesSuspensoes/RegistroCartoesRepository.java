package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.List;

public interface RegistroCartoesRepository {
    void salvarCartao(RegistroCartao cartao);
    List<RegistroCartao> buscarCartoesPorAtleta(String atleta);
    void limparCartoes(String atleta);
}
