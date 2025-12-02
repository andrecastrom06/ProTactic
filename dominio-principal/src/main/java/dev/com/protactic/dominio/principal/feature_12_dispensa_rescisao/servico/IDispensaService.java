package dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;

public interface IDispensaService {
    void dispensarJogador(Jogador jogador, Usuario usuarioSolicitante) throws Exception;

    void dispensarJogador(Jogador jogador) throws Exception;
}