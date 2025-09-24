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
        if (jogador.getContrato() != null && !jogador.getContrato().isExpirado()) {
            // jogador já tem contrato ativo
            return false;
        }

        if (!janelaAberta) {
            // fora da janela
            return false;
        }

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
