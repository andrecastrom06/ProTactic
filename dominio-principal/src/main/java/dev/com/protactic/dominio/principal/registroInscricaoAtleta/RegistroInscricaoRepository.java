package dev.com.protactic.dominio.principal.registroInscricaoAtleta;

import java.util.List;

import dev.com.protactic.dominio.principal.InscricaoAtleta;

public interface RegistroInscricaoRepository {

    void salvar(InscricaoAtleta inscricao);

    InscricaoAtleta buscarPorAtletaECompeticao(String atleta, String competicao);

    List<InscricaoAtleta> listarTodas();
}
