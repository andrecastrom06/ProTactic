package dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;

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