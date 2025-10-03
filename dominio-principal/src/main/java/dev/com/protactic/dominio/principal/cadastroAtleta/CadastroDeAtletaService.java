package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.*;

public class CadastroDeAtletaService {

    private final IJogadorRepository jogadorRepo;
    private final IClubeRepository clubeRepo;

    public CadastroDeAtletaService(IJogadorRepository jogadorRepo, IClubeRepository clubeRepo) {
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
    }

    public boolean contratar(Clube clubeDestino, Jogador jogador, boolean janelaAberta) {
        // Caso 1: jogador com contrato ativo
        if (jogador.getContrato() != null && !jogador.getContrato().isExpirado()) {
            if (!janelaAberta) {
                return false; // só pode se a janela estiver aberta
            }
        }

        // Caso 2: jogador sem contrato ou contrato expirado
        // -> pode contratar em qualquer data (mesmo fora da janela)

        // cria novo contrato
        Contrato novoContrato = new Contrato(clubeDestino);
        jogador.setContrato(novoContrato);
        clubeDestino.adicionarJogador(jogador);

        // persiste
        jogadorRepo.salvar(jogador);
        clubeRepo.salvar(clubeDestino);

        return true;
    }
}
