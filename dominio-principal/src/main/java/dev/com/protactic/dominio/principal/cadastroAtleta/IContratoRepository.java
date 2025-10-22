package dev.com.protactic.dominio.principal.cadastroAtleta;

import java.util.List;

import dev.com.protactic.dominio.principal.Contrato;

public interface IContratoRepository {

    /**
     * Salva (cria ou atualiza) um agregado Contrato.
     * Se o contrato for novo (id=0), deve atribuir um novo ID.
     * @param contrato O agregado a ser salvo.
     */
    void salvar(Contrato contrato);

    /**
     * Busca um Contrato pelo seu ID.
     * @param id O ID do contrato.
     * @return O agregado Contrato encontrado ou null se não existir.
     */
    Contrato buscarPorId(Integer id);

    /**
     * Lista todos os contratos cadastrados.
     * @return Uma lista de todos os Contratos.
     */
    List<Contrato> listarTodos();
    }
