package dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.repositorio;

import java.util.List;

import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.entidade.InscricaoAtleta;

public interface RegistroInscricaoRepository {

    void salvar(InscricaoAtleta inscricao);

    InscricaoAtleta buscarPorAtletaECompeticao(String atleta, String competicao);

    List<InscricaoAtleta> listarTodas();
}
