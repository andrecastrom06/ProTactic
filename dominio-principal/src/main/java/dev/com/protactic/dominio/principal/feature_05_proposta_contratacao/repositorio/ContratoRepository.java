package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;

public interface ContratoRepository {

    void salvar(Contrato contrato);

    Contrato buscarPorId(Integer id);


    List<Contrato> listarTodos();
    }
