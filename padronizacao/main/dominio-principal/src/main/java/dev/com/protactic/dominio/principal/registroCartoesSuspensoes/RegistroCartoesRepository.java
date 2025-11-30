package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.List;

import dev.com.protactic.dominio.principal.RegistroCartao;

public interface RegistroCartoesRepository {
    void salvarCartao(RegistroCartao cartao);
    List<RegistroCartao> buscarCartoesPorAtleta(String atleta);
    void limparCartoes(String atleta);
}
