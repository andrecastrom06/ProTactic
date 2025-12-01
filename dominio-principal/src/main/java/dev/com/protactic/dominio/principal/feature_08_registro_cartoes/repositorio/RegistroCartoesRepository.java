package dev.com.protactic.dominio.principal.feature_08_registro_cartoes.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade.RegistroCartao;

public interface RegistroCartoesRepository {
    void salvarCartao(RegistroCartao cartao);
    List<RegistroCartao> buscarCartoesPorAtleta(String atleta);
    void limparCartoes(String atleta);
}
