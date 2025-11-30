package dev.com.protactic.dominio.principal.cadastroAtleta;

import java.util.List;

import dev.com.protactic.dominio.principal.Contrato;

public interface ContratoRepository {

    void salvar(Contrato contrato);

    Contrato buscarPorId(Integer id);


    List<Contrato> listarTodos();
    }
