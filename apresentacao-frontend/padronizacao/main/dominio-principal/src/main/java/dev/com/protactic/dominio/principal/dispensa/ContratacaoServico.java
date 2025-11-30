package dev.com.protactic.dominio.principal.dispensa;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;

import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

public class ContratacaoServico {

    private final ClubeRepository clubeRepo;
    private final JogadorRepository jogadorRepo;
    private final ContratoRepository contratoRepo;

    public ContratacaoServico(ClubeRepository clubeRepo, JogadorRepository jogadorRepo, ContratoRepository contratoRepo) {
        this.clubeRepo = clubeRepo;
        this.jogadorRepo = jogadorRepo;
        this.contratoRepo = contratoRepo;
    }

    public boolean registrarAtleta(Clube clubeDestino, Jogador jogador, boolean janelaDeTransferenciaAberta) {
        
        if (jogador.getClubeId() == null) {
            
            Contrato novoContrato = new Contrato(clubeDestino.getId());
            
            contratoRepo.salvar(novoContrato);

            clubeDestino.adicionarJogadorId(jogador.getId());
            
            jogador.setContratoId(novoContrato.getId());
            jogador.setClubeId(clubeDestino.getId());

            clubeRepo.salvar(clubeDestino);
            jogadorRepo.salvar(jogador);
            
            return true;
        }

        if (jogador.getClubeId() != null && !janelaDeTransferenciaAberta) {
            System.out.println("Falha ao contratar " + jogador.getNome() + ": A janela de transferências está fechada.");
            return false;
        }
        
        return false;
    }
}