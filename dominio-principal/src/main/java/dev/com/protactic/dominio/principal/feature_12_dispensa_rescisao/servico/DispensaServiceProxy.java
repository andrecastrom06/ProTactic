package dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;

public class DispensaServiceProxy implements IDispensaService {

    private final IDispensaService servicoReal;

    public DispensaServiceProxy(IDispensaService servicoReal) {
        this.servicoReal = servicoReal;
    }

    @Override
    public void dispensarJogador(Jogador jogador, Usuario usuarioSolicitante) throws Exception {
        if (!isAnalista(usuarioSolicitante)) {
            throw new SecurityException("ACESSO NEGADO: Apenas usuários com função 'ANALISTA' podem dispensar jogadores.");
        }

        System.out.println("AUDITORIA: O Analista " + usuarioSolicitante.getNome() + " solicitou a dispensa do atleta " + jogador.getNome());
        servicoReal.dispensarJogador(jogador, usuarioSolicitante);
    }

    @Override
    public void dispensarJogador(Jogador jogador) throws Exception {
        System.out.println("AUDITORIA SISTEMA: Dispensa automática processada para " + jogador.getNome());
        servicoReal.dispensarJogador(jogador);
    }

    private boolean isAnalista(Usuario usuario) {
        return usuario != null && "ANALISTA".equalsIgnoreCase(usuario.getFuncao()); 
    }
}